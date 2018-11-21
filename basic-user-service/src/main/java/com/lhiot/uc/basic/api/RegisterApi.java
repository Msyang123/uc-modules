package com.lhiot.uc.basic.api;

import com.leon.microx.web.result.Id;
import com.lhiot.uc.basic.entity.ApplyUser;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class RegisterApi {


    private static final String USER_REGISTER_CACHE = ":user:register";
    private final RedissonClient redissonClient;
    private final RegisterService registerService;
    private final UserBindingService userBindingService;

    public RegisterApi(RedissonClient redissonClient, RegisterService registerService, UserBindingService userBindingService) {
        this.redissonClient = redissonClient;
        this.registerService = registerService;
        this.userBindingService = userBindingService;
    }


    @ApiOperation(value = "手机号注册",response = Id.class)
    @ApiImplicitParam(paramType = "body", name = "param", value = "手机号码及密码，验证码", required = true, dataType = "PhoneRegisterParam")
    @PostMapping(value = "/phone/users")
    public ResponseEntity registerByPhone(@RequestBody PhoneRegisterParam param) {
        RMapCache<String, String> cache = redissonClient.getMapCache(param.getPhone() + USER_REGISTER_CACHE);
        String phone = cache.get(param.getApplicationType() + ":" + param.getPhone() + USER_REGISTER_CACHE);
        if (Objects.equals(param.getPhone(), phone)) {
            return ResponseEntity.badRequest().body("正在注册中！");
        }
        cache.put(param.getPhone() + USER_REGISTER_CACHE, param.getPhone(), 2, TimeUnit.MINUTES);
        try {
            if (registerService.hasPhone(param.getPhone(), param.getApplicationType())) {
                return ResponseEntity.badRequest().body("手机号码已注册!");
            }
            UserDetailResult result = registerService.register(param);
            return ResponseEntity.created(URI.create("/users/"+result.getId())).body(Id.of(result.getId()));
        } catch (Exception e) {
            cache.put(param.getApplicationType() + ":" + param.getPhone() + USER_REGISTER_CACHE, param.getPhone(), 5, TimeUnit.SECONDS);
            log.error("手机注册失败异常",e);
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "微信注册",response = Id.class)
    @ApiImplicitParam(paramType = "body", name = "param", value = "微信用户信息", required = true, dataType = "WeChatRegisterParam")
    @PostMapping(value = "/we-chat/users")
    public ResponseEntity registerByWeChat(@RequestBody WeChatRegisterParam param) {
        if (registerService.hasOpenId(param.getOpenId())) {
            return ResponseEntity.badRequest().body("微信已注册!");
        }
        ApplyUser user = new ApplyUser();
        user.setPhone(param.getPhone());
        user.setApplicationType(param.getApplicationType());
        if (userBindingService.isBindingWeChat(user)) {
            return ResponseEntity.badRequest().body("手机号已绑定微信！");
        }
        UserDetailResult result = registerService.registerByOpenId(param);
        return ResponseEntity.created(URI.create("/users/"+result.getId())).body(Id.of(result.getId().toString()));
    }

}
