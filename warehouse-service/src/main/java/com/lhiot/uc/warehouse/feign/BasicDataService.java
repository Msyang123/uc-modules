package com.lhiot.uc.warehouse.feign;

import com.lhiot.uc.warehouse.conversion.CategoryItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author zhangfeng create in 14:24 2018/10/15
 */
@FeignClient("basic-data-service-v1-0")
public interface BasicDataService {

    @RequestMapping(value = "/category/{cgCode}/category-item/{code}",method = RequestMethod.GET)
    ResponseEntity<CategoryItem> findCategoryByCode(@PathVariable("cgCode") String cgCode, @PathVariable("code") String code);
}
