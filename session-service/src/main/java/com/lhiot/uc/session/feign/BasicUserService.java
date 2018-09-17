package com.lhiot.uc.session.feign;

import com.lhiot.uc.session.model.Apply;
import com.lhiot.uc.session.model.LoginResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author zhangfeng created in 2018/9/13 9:19
 **/
@FeignClient("basic-user-service-v1-0")
@Component
public interface BasicUserService {

    @RequestMapping(value = "/users/phone/{phoneNumber}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByPhone(@PathVariable("phoneNumber") String phone, @RequestParam("apply") Apply apply);

    @RequestMapping(value = "/users/password/{id}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> determineLoginPassword(@PathVariable("id") Long userId, @RequestParam("password") String password);

    @RequestMapping(value = "/users/open-id/{openId}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByOpenId(@PathVariable("openId") String openId);

    @RequestMapping(value = "/users/user-id/{userId}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByUserId(@PathVariable("userId") Long userId);
}
