package com.lhiot.uc.warehouse.aspect;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng create in 14:42 2018/10/15
 */
@Data
@ApiModel
public class CategoryItem {
    /**
     *id
     */
    @ApiModelProperty(value = "id", dataType = "Long")
    private Long id;

    /**
     *字典编码
     */
    @ApiModelProperty(value = "字典编码", dataType = "String")
    private String cgCode;

    /**
     *字典项名称
     */
    @ApiModelProperty(value = "字典项名称", dataType = "String")
    private String name;

    /**
     *字典项编码
     */
    @ApiModelProperty(value = "字典项编码", dataType = "String")
    private String code;

    /**
     *排序序号
     */
    @ApiModelProperty(value = "排序序号", dataType = "Long")
    private Long sort;

    /**
     *附加参数
     */
    @ApiModelProperty(value = "附加参数", dataType = "String")
    private String attache;
}
