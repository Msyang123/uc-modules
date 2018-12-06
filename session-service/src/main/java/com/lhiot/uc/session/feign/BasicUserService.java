package com.lhiot.uc.session.feign;

import com.lhiot.uc.session.model.ApplicationType;
import com.lhiot.uc.session.model.LoginResult;
import com.lhiot.uc.session.model.SearchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangfeng created in 2018/9/13 9:19
 **/
@FeignClient("basic-user-service-v1-0")
@Component
public interface BasicUserService {

    @RequestMapping(value = "/users/phone/{phoneNumber}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByPhone(@PathVariable("phoneNumber") String phone, @RequestParam("applicationType") String applicationType);

    @RequestMapping(value = "/users/phone-and-password/search", method = RequestMethod.POST)
    ResponseEntity<LoginResult> getUserByPhoneAndPassword(@RequestBody SearchParam param);

    @RequestMapping(value = "/users/open-id/{openId}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByOpenId(@PathVariable("openId") String openId);

    @RequestMapping(value = "/users/user-id/{userId}", method = RequestMethod.GET)
    ResponseEntity<LoginResult> getUserByUserId(@PathVariable("userId") Long userId);
}
