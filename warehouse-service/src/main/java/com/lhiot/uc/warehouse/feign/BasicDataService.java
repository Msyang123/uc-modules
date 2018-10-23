package com.lhiot.uc.warehouse.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangfeng create in 11:47 2018/10/23
 */
@FeignClient("basic-data-service-v1-0")
@Component
public interface BasicDataService {

    @RequestMapping(value = "/product/specification/{id}",method = RequestMethod.GET)
    ResponseEntity findProductBySpecificationId(@PathVariable("id") Long id);
}
