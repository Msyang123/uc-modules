package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng created in 2018/9/20 16:29
 **/
@Data
@ApiModel
public class PasswordParam {
    @ApiModelProperty(notes = "支付密码",dataType = "String")
    private String paymentPassword;
    @ApiModelProperty(notes = "登录密码",dataType = "String")
    private String password;
}
