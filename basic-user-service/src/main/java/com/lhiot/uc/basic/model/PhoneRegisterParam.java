package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class PhoneRegisterParam {
    @ApiModelProperty(notes = "电话号码", dataType = "String", required = true)
    @NotNull
//    @Pattern(regexp = ValidateConstant.mobileRegex, message = "请输入正确的电话号码")
    private String userMobile;


    @ApiModelProperty(notes = "验证码", dataType = "String", required = false)
    @Pattern(regexp = "\\w{6}", message = "请输入正确的验证码")
    private String verifyCode;

    @ApiModelProperty(notes = "密码", dataType = "String", required = false)
    private String userPassword;

    @ApiModelProperty(notes = "应用类型",dataType = "String")
    private String apply;

}
