package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng created in 2018/9/7 17:12
 **/
@Data
@ApiModel
public class UserBindingWeChatParam {
    @ApiModelProperty(notes = "微信openId", dataType = "String")
    private String openId;
    @ApiModelProperty(notes = "微信unionId", dataType = "String")
    private String unionId;
    @ApiModelProperty(notes = "业务用户Id", dataType = "Long")
    private Long applyUserId;
}
