package com.lhiot.uc.session.api;

import com.leon.microx.util.Maps;
import com.leon.microx.web.swagger.ApiHideBodyProperty;
import com.lhiot.uc.session.feign.BasicUserService;
import com.lhiot.uc.session.feign.ThirdPartyService;
import com.lhiot.uc.session.feign.WarehouseService;
import com.lhiot.uc.session.mapper.SessionMapper;
import com.lhiot.uc.session.model.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static org.springframework.http.HttpStatus.SEE_OTHER;

/**
 * @author Leon (234239150@qq.com) created in 9:35 18.8.30
 */
@Slf4j
@RestController
public class SessionApi {

    private static final String LOGIN_SMS_TEMPLATE_NAME = "from-login";
    private static final String FAILURE_MESSAGE = "检测到您的账号存在数据异常，无法登录";
    private BasicUserService basicUserService;
    private ThirdPartyService thirdPartyService;
    private WarehouseService warehouseService;
    private SessionMapper sessionMapper;

    public SessionApi(BasicUserService basicUserService, ThirdPartyService thirdPartyService, WarehouseService warehouseService, SessionMapper sessionMapper) {
        this.basicUserService = basicUserService;
        this.thirdPartyService = thirdPartyService;
        this.warehouseService = warehouseService;
        this.sessionMapper = sessionMapper;
    }

    @ApiOperation(value = "手机密码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"captcha", "userId", "sessionId", "loginAt", "openId"})
    @PostMapping("/password/sessions")
    public ResponseEntity cellphoneLogin(@RequestBody LoginParam param) {
        SearchParam searchParam = new SearchParam();
        searchParam.setPhone(param.getPhone());
        searchParam.setPassword(param.getPassword());
        searchParam.setApplicationType(param.getApplicationType());
        ResponseEntity response = basicUserService.getUserByPhoneAndPassword(searchParam);
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.badRequest().body("用户不存在或密码错误！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body(FAILURE_MESSAGE);
        }
        result.setWarehouseId(this.warehouseId(result.getBaseUserId()));
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "手机验证码登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt"})
    @PostMapping("/captcha/sessions")
    public ResponseEntity captchaSession(@RequestBody LoginParam param) {
        ResponseEntity response = basicUserService.getUserByPhone(param.getPhone(), param.getApplicationType());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body(FAILURE_MESSAGE);
        }
        ResponseEntity thirdPartyResponse = thirdPartyService.validate(LOGIN_SMS_TEMPLATE_NAME, param.getPhone(), Maps.of("number", param.getCaptcha()));
        if (!thirdPartyResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.badRequest().body(response.hasBody() ? response.getBody() : "验证码错误！");
        }
        result.setWarehouseId(this.warehouseId(result.getBaseUserId()));
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "微信登录", response = LoginResult.class)
    @ApiHideBodyProperty({"password", "userId", "sessionId", "loginAt", "captcha", "phone"})
    @PostMapping("/we-chat/sessions")
    public ResponseEntity weChatSession(@RequestBody LoginParam param) {
        ResponseEntity response = basicUserService.getUserByOpenId(param.getOpenId());
        if (Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful() || Objects.isNull(response.getBody())) {
            return ResponseEntity.status(SEE_OTHER).body("用户不存在，去注册！");
        }
        LoginResult result = (LoginResult) response.getBody();
        if (LockStatus.LOCK.equals(result.getLocked())) {
            return ResponseEntity.badRequest().body(FAILURE_MESSAGE);
        }
        result.setWarehouseId(this.warehouseId(result.getBaseUserId()));
        sessionMapper.insert(param);
        return ResponseEntity.ok().body(result);
    }

    private Long warehouseId(Long baseUserId) {
        ResponseEntity warehouseResponse = warehouseService.findWarehouse(baseUserId);
        if (Objects.nonNull(warehouseResponse) && warehouseResponse.getStatusCode().is2xxSuccessful()) {
            WarehouseUser warehouseUser = (WarehouseUser) warehouseResponse.getBody();
            return Objects.isNull(warehouseUser)?null:warehouseUser.getId();
        }
        return null;
    }
}
