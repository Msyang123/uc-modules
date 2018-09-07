package com.lhiot.uc.basic.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Author zhangfeng created in 2018/9/7 16:34
 **/
@FeignClient("thirdparty-service-v1-0")
@Component
public interface ThirdPartyService {

    @RequestMapping(value = "/sms-captcha/{template}/{mobile}/sms/validate",method = RequestMethod.POST)
    ResponseEntity validate(@PathVariable("template") String template, @PathVariable("mobile") String mobile, @RequestBody Map<String,Object> param);
}
