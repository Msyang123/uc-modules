package com.lhiot.uc.session.feign;

import com.lhiot.uc.session.model.WarehouseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangfeng create in 14:49 2018/10/10
 */
@FeignClient("warehouse-service-v1-0")
@Component
public interface WarehouseService {

    @RequestMapping(value = "/warehouse/base-users/{baseUserId}", method = RequestMethod.GET)
    ResponseEntity<WarehouseUser> findWarehouse(@PathVariable("baseUserId") Long baseUserId);
}
