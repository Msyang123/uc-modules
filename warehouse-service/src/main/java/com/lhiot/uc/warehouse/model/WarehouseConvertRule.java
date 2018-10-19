package com.lhiot.uc.warehouse.model;

import lombok.Data;

/**
 * @author zhangfeng create in 16:00 2018/10/19
 */
@Data
public class WarehouseConvertRule {

    private String rulesName;

    private String rulesDescription;

    private Integer freshDay;

    private Integer convertFirst;

    private Integer firstDiscount;

    private Integer convertSecond;

    private Integer secondDiscount;

    private Integer convertThird;

    private Integer thirdDiscount;

    private Integer systemDiscount;

}
