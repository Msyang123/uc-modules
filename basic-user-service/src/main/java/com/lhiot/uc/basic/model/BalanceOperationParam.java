package com.lhiot.uc.basic.model;

import com.lhiot.dc.dictionary.HasEntries;
import com.lhiot.uc.basic.entity.OperationStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng created in 2018/9/12 9:22
 **/
@Data
@ApiModel
public class BalanceOperationParam {

    @ApiModelProperty(notes = "加减金额", dataType = "Long")
    private Long money;
    @ApiModelProperty(notes = "加减操作标识：SUBTRACT - 减，ADD-加", dataType = "OperationStatus")
    private OperationStatus operation;
    @ApiModelProperty(notes = "来源", dataType = "String")
    private String memo;
    @ApiModelProperty(notes = "来源ID：例如：订单ID，活动ID", dataType = "String")
    private String sourceId;
    @HasEntries(from = "applications")
    @ApiModelProperty(notes = "来源用户", dataType = "String")
    private String applicationType;
    @ApiModelProperty(notes = "支付密码",dataType = "String")
    private String paymentPassword;
}
