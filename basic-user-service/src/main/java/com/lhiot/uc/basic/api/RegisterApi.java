package com.lhiot.uc.basic.api;

import com.leon.microx.util.SnowflakeId;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.model.WechatRegisterParam;
import com.lhiot.uc.basic.service.RegisterService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/user")
public class RegisterApi {

    private static final int RETRY_COUNT = 2;
    private final RedissonClient redissonClient;
    private final SnowflakeId snowflakeId;
    private final RegisterService registerService;

    public RegisterApi(RedissonClient redissonClient, SnowflakeId snowflakeId, RegisterService registerService) {
        this.redissonClient = redissonClient;
        this.snowflakeId = snowflakeId;
        this.registerService = registerService;
    }


    @ApiOperation(value = "手机号注册", notes = "进行注册")
    @ApiImplicitParam(paramType = "body", name = "param", value = "手机号码及密码，验证码", required = true, dataType = "PhoneRegisterParam")
    @PostMapping(value = "/phone/register")
    public ResponseEntity registerByPhone(@RequestBody PhoneRegisterParam param) {
        RMapCache<String, String> cache = redissonClient.getMapCache(param.getPhone() + ":user:register");
        String phone = cache.get(param.getPhone() + ":user:register");
        if (Objects.equals(param.getPhone(), phone)) {
            return ResponseEntity.badRequest().body("正在注册中！");
        }
        cache.put(param.getPhone() + ":user:register", param.getPhone(), 2, TimeUnit.MINUTES);
        try {
//        valueOperations.set(param.getUserMobile() + ":user:register", param.getUserMobile(), 3, TimeUnit.MINUTES);
            //TODO 从redis中获取手机验证码
//        String cacheKey = sms.getSmsCacheKey(TEMPLATE_NAME, param.getUserMobile());
//        SmsInfo smsInfo = smsInfoOperations.get(cacheKey);
//        // 验证是否填写正确
//        boolean verify = Objects.nonNull(smsInfo) && Objects.equals(smsInfo.getContent(), param.getVerifyCode());
//        if (!verify) {
//            return ResponseEntity.badRequest().body("验证码错误");
//        }
            if (registerService.registered(phone)){
                return ResponseEntity.badRequest().body("手机号码已注册!");
            }
            UserDetailResult result = registerService.register(param);

            // 判断用户是否存在
            if (registerService.count(null, param.getPhone()) > 0) {
                return ResponseEntity.badRequest().body("手机号码已注册!");
            }
            // 保存注册用户信息
            UserDetailResult userRegister = registerService.addRegisterUser(param);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            cache.put(param.getPhone() + ":user:register", param.getPhone(), 5, TimeUnit.SECONDS);
            return ResponseEntity.badRequest().build();
        }
    }

//    @ApiOperation("微信注册")
//    @ApiImplicitParam(paramType = "body", name = "param", value = "微信用户信息", required = true, dataType = "WechatRegisterParam")
//    @PostMapping(value = "/wechat/register")
//    public ResponseEntity registerByWechat(WechatRegisterParam param){
//
//    }
}
