package com.lhiot.uc.warehouse.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng create in 8:52 2018/10/12
 */
@Data
@ApiModel
public class WarehouseConvertParam {

    private Long warehouseId;

    @ApiModelProperty(value = "当前页,默认值1")
    private Long page = 1L;

    /**
     * 传入-1可不分页
     */
    @ApiModelProperty(value = "每页显示条数,默认值10")
    private Long rows = 10L;

    @ApiModelProperty(value = "分页查询从第几条开始")
    private Long startRow;
}
