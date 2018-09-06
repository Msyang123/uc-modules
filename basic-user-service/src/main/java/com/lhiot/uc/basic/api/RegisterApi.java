package com.lhiot.uc.basic.api;

import com.leon.microx.util.SnowflakeId;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.service.RegisterService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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


    @SuppressWarnings("unchecked")
    @ApiOperation(value = "注册", notes = "进行注册")
    @ApiImplicitParam(paramType = "body", name = "param", value = "手机号码及密码，验证码", required = true, dataType = "PhoneRegisterParam")
    @PostMapping(value = "/register")
    @Transactional
    public ResponseEntity registerByPhone(@RequestBody PhoneRegisterParam param){
        log.info("注册--param:{}", param.toString());
       RMapCache<String,String> cache =  redissonClient.getMapCache(param.getUserMobile() + ":user:register");
        String phone =  cache.get(param.getUserMobile() + ":user:register");
//        String phone = (String) valueOperations.get(param.getUserMobile() + ":user:register");
        if (Objects.equals(param.getUserMobile(), phone)) {
            return ResponseEntity.badRequest().body("正在注册中！");
        }
        // 将用户电话缓存到redis做幂等
        cache.put(param.getUserMobile() + ":user:register",param.getUserMobile(),2,TimeUnit.MINUTES);
//        valueOperations.set(param.getUserMobile() + ":user:register", param.getUserMobile(), 3, TimeUnit.MINUTES);

        // FIXME 当注册失败，则需等待redis缓存清除
        // 从redis中获取手机验证码
//        String cacheKey = sms.getSmsCacheKey(TEMPLATE_NAME, param.getUserMobile());
//        SmsInfo smsInfo = smsInfoOperations.get(cacheKey);
//        // 验证是否填写正确
//        boolean verify = Objects.nonNull(smsInfo) && Objects.equals(smsInfo.getContent(), param.getVerifyCode());
//        if (!verify) {
//            return ResponseEntity.badRequest().body("验证码错误");
//        }
        // 判断用户是否存在
        if (registerService.count(null,param.getUserMobile()) > 0) {
            return ResponseEntity.badRequest().body("手机号码已注册!");
        }

        // 注册产生的用户ID
        Long userId = snowflakeId.longId();
        // 保存注册用户信息
        UserDetailResult userRegister = registerService.addRegisterUser(param);
        return ResponseEntity.ok(userRegister);

    }

}
