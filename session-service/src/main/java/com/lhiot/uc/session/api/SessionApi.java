package com.lhiot.uc.session.api;

import com.leon.microx.support.swagger.ApiHideBodyProperty;
import com.leon.microx.util.Maps;
import com.lhiot.uc.session.feign.BasicUserService;
import com.lhiot.uc.session.feign.ThirdPartyService;
import com.lhiot.uc.session.mapper.SessionMapper;
import com.lhiot.uc.session.model.LockStatus;
import com.lhiot.uc.session.model.LoginParam;
import com.lhiot.uc.session.model.LoginResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.HttpStatus.SEE_OTHER;

/**
 * @author Leon (234239150@qq.com) created in 9:35 18.8.30
 */
@Slf4j
@RestController
public class SessionApi {

    private static final String LOGIN_SMS_TEMPLATE_NAME = "from-login";
    private BasicUserService basicUserService;
    private ThirdPartyService thirdPartyService;
    private SessionMapper sessionMapper;

    public SessionApi(BasicUserService basicUserService, ThirdPartyService thirdPartyService, SessionMapper sessionMapper) {
        this.basicUserService = basicUserService;
        this.thirdPartyService = thirdPartyService;
        this.sessionMapper = sessionMapper;
    }

    @ApiOperation(value = "手机密码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"captcha", "userId", "sessionId", "loginAt", "openId"})
    @PostMapping("/password/session")
    public ResponseEntity cellphoneLogin(@RequestBody LoginParam param) {
        ResponseEntity response = basicUserService.getUserByPhone(param.getPhone(), param.getApplicationType());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录");
        }
        ResponseEntity determineResponse = basicUserService.determineLoginPassword(result.getId(), param.getPassword());
        if (Objects.isNull(determineResponse) || !determineResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body("密码不正确！");
        }
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "手机验证码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt"})
    @PostMapping("/captcha/session")
    public ResponseEntity captchaSession(@RequestBody LoginParam param) {
        ResponseEntity response = basicUserService.getUserByPhone(param.getPhone(), param.getApplicationType());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录");
        }
        ResponseEntity thirdPartyResponse = thirdPartyService.validate(LOGIN_SMS_TEMPLATE_NAME, param.getPhone(), Maps.of("number", param.getCaptcha()));
        if (!thirdPartyResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body(response.hasBody() ? response.getBody() : "验证码错误！");
        }
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "微信登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt", "captcha", "phone"})
    @GetMapping("/we-chat/session")
    public ResponseEntity weChatSession(@RequestBody LoginParam param) {
        ResponseEntity response = basicUserService.getUserByOpenId(param.getOpenId());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录");
        }
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }
}
