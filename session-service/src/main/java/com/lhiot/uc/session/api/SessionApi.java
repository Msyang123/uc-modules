package com.lhiot.uc.session.api;

import com.leon.microx.support.session.Sessions;
import com.leon.microx.support.swagger.ApiHideBodyProperty;
import com.lhiot.uc.session.feign.BasicUserService;
import com.lhiot.uc.session.model.LockStatus;
import com.lhiot.uc.session.model.LoginParam;
import com.lhiot.uc.session.model.LoginResult;
import com.lhiot.uc.session.service.SessionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

import static org.springframework.http.HttpStatus.SEE_OTHER;

/**
 * @author Leon (234239150@qq.com) created in 9:35 18.8.30
 */
@Slf4j
@RestController
public class SessionApi {

    private static final String LOGIN_SMS_TEMPLATE_NAME = "";
    private final Sessions session;
    private SessionService service;
    private BasicUserService basicUserService;

    public SessionApi(Sessions session, SessionService service, BasicUserService basicUserService) {
        this.session = session;
        this.service = service;
        this.basicUserService = basicUserService;
    }

    @Sessions.Uncheck
    @ApiOperation(value = "手机密码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"captcha", "userId", "sessionId", "loginAt", "openId"})
    @PostMapping("/password/session")
    public ResponseEntity cellphoneLogin(@RequestBody LoginParam param, @ApiIgnore HttpServletRequest request) {
        // TODO Feign调用基础服务 查询用户信息：用户ID、用户名、可以访问的API集合（ANT匹配）
        ResponseEntity response = basicUserService.getUserByPhone(param.getPhone(), param.getApply());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录如有疑问请联系客服0731-85240088");
        }
        ResponseEntity determineResponse = basicUserService.determineLoginPassword(result.getId(), param.getPassword());
        if (Objects.isNull(determineResponse) || !determineResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body("密码不正确！");
        }
        param.setSessionId(service.createSession(result, request));
        param.setUserId(result.getId());
        return this.sessionResult(param, result);
    }

    @ApiOperation(value = "手机验证码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt"})
    @PostMapping("/captcha/session")
    public ResponseEntity captchaSession(@RequestBody LoginParam param, @ApiIgnore HttpServletRequest request) {
        ResponseEntity response = basicUserService.getUserByPhone(param.getPhone(), param.getApply());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录如有疑问请联系客服0731-85240088");
        }
//        ResponseEntity thirdPartyResponse = thirdPartyService.validate(LOGIN_SMS_TEMPLATE_NAME, param.getPhoneNumber(), ImmutableMap.of("number", param.getCaptcha()));
//        if (!thirdPartyResponse.getStatusCode().is2xxSuccessful()) {
//            return ResponseEntity.badRequest().body(response.hasBody() ? response.getBody() : "验证码错误！");
//        }
        param.setSessionId(service.createSession(result, request));
        param.setUserId(result.getId());
        return this.sessionResult(param, result);
    }

    @ApiOperation(value = "微信登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt", "captcha", "phoneNumber"})
    @GetMapping("/we-chat/session")
    public ResponseEntity weChatSession(@RequestBody LoginParam param, @ApiIgnore HttpServletRequest request) {
        ResponseEntity response = basicUserService.getUserByOpenId(param.getOpenId());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body("检测到您的账号存在数据异常，无法登录如有疑问请联系客服0731-85240088");
        }
        param.setSessionId(service.createSession(result, request));
        param.setUserId(result.getId());
        return this.sessionResult(param, result);
    }

    private ResponseEntity sessionResult(LoginParam param, LoginResult result) {
        try {
            // 返回session
            return ResponseEntity.ok()
                    .header(Sessions.HTTP_HEADER_NAME, param.getSessionId())
                    .body(result);
        } finally {
            // 更新登录状态
            service.online(param);
        }
    }

    @ApiOperation("退出登录")
    @DeleteMapping("/session")
    public ResponseEntity logout(Sessions.User current, @ApiIgnore HttpServletRequest request) {
        String sessionId = Sessions.id(request);
        session.invalidate(sessionId);
        service.logout(current);
        log.info("user {} logout.", current);
        return ResponseEntity.noContent().build();
    }
}
