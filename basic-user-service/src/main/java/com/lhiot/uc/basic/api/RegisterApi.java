package com.lhiot.uc.basic.api;

import com.google.common.collect.ImmutableMap;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.feign.ThirdPartyService;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.model.WeChatRegisterParam;
import com.lhiot.uc.basic.service.RegisterService;
import com.lhiot.uc.basic.service.UserBindingService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/users")
public class RegisterApi {

    private static final String REGIST_SMS_TEMPLATE_NAME = "common-verification";
    private final RedissonClient redissonClient;
    private final RegisterService registerService;
    private final ThirdPartyService thirdPartyService;
    private final UserBindingService userBindingService;

    public RegisterApi(RedissonClient redissonClient, RegisterService registerService, ThirdPartyService thirdPartyService, UserBindingService userBindingService) {
        this.redissonClient = redissonClient;
        this.registerService = registerService;
        this.thirdPartyService = thirdPartyService;
        this.userBindingService = userBindingService;
    }


    @ApiOperation(value = "手机号注册", notes = "进行注册")
    @ApiImplicitParam(paramType = "body", name = "param", value = "手机号码及密码，验证码", required = true, dataType = "PhoneRegisterParam")
    @PostMapping(value = "/phone/register")
    public ResponseEntity registerByPhone(@RequestBody PhoneRegisterParam param) {
        RMapCache<String, String> cache = redissonClient.getMapCache(param.getPhone() + ":user:register");
        String phone = cache.get(param.getApply()+":"+param.getPhone() + ":user:register");
        if (Objects.equals(param.getPhone(), phone)) {
            return ResponseEntity.badRequest().body("正在注册中！");
        }
//        cache.put(param.getPhone() + ":user:register", param.getPhone(), 2, TimeUnit.MINUTES);
//        ResponseEntity response = thirdPartyService.validate(REGIST_SMS_TEMPLATE_NAME, param.getPhone(), ImmutableMap.of("number", param.getVerifyCode()));
//        if (response.getStatusCode().is4xxClientError()) {
//            return ResponseEntity.badRequest().body(response.hasBody() ? response.getBody() : "验证码错误！");
//        }
        try {
            if (registerService.hasPhone(param.getPhone(), param.getApply())) {
                return ResponseEntity.badRequest().body("手机号码已注册!");
            }
            UserDetailResult result = registerService.register(param);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            cache.put(param.getApply()+":"+param.getPhone() + ":user:register", param.getPhone(), 5, TimeUnit.SECONDS);
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation("微信注册")
    @ApiImplicitParam(paramType = "body", name = "param", value = "微信用户信息", required = true, dataType = "WeChatRegisterParam")
    @PostMapping(value = "/we-chat/register")
    public ResponseEntity registerByWechat(@RequestBody WeChatRegisterParam param) {
        if (registerService.hasOpenId(param.getOpenId())) {
            return ResponseEntity.badRequest().body("微信已注册已注册!");
        }
        ApplyUser user = new ApplyUser();
        user.setPhone(param.getPhone());
        user.setApply(param.getApply());
        if (userBindingService.isBindingWeChat(user)){
            return ResponseEntity.badRequest().body("手机号已绑定微信！");
        }
        UserDetailResult result = registerService.registerByOpenId(param);
        return ResponseEntity.ok(result);
    }

}
