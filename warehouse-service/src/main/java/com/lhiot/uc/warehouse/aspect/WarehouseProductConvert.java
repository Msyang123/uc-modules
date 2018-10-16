package com.lhiot.uc.warehouse.aspect;

import java.lang.annotation.*;

/**
 * @author zhangfeng create in 8:52 2018/10/15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WarehouseProductConvert {
    String warehouseId();

    RuleType rule() default RuleType.WAREHOUSE_RULE;
}
