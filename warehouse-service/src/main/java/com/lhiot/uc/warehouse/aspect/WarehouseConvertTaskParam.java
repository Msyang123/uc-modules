package com.lhiot.uc.warehouse.aspect;

import lombok.Data;

/**
 * @author zhangfeng create in 9:07 2018/10/16
 */
@Data
public class WarehouseConvertTaskParam {
    private String warehouseId;

    private Integer freshDay;


    private Integer systemConvertDay;

    private Integer systemDiscount;

}
