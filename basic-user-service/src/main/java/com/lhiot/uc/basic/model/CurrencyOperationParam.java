package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.entity.OperationStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangfeng created in 2018/9/12 9:22
 **/
@Data
@ApiModel
public class CurrencyOperationParam {

    @ApiModelProperty(notes = "基础用户ID", dataType = "Long")
    private Long baseUserId;
    @ApiModelProperty(notes = "加减余额", dataType = "Long")
    private Long operationCurrency;
    @ApiModelProperty(notes = "加减操作标识：SUBTRACT - 减，ADD-加", dataType = "OperationStatus")
    private OperationStatus operation;
    @ApiModelProperty(notes = "来源", dataType = "String")
    private String sourceType;
    @ApiModelProperty(notes = "来源ID：例如：订单ID，活动ID", dataType = "String")
    private String sourceId;
    @ApiModelProperty(notes = "来源用户", dataType = "Apply")
    private Apply apply;
}
