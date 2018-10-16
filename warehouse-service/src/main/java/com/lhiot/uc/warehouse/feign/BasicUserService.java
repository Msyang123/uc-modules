package com.lhiot.uc.warehouse.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangfeng create in 8:44 2018/10/16
 */
@FeignClient("basic-user-service-v1-0")
public interface BasicUserService {

    @RequestMapping(value = "/users/{id}/balance/{money}",method = RequestMethod.PUT)
    ResponseEntity addBalance(@PathVariable("id") Long id,@PathVariable("money") Integer money);
}
