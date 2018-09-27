package com.lhiot.uc.session.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

/**
 * @author Leon (234239150@qq.com) created in 9:36 18.8.30
 */
@Data
@ApiModel
public class LoginParam {

    @ApiModelProperty(notes = "手机号码", dataType = "String")
    private String phone;
    @ApiModelProperty(notes = "密码", dataType = "String")
    private String password;
    @ApiModelProperty(notes = "用户编号", dataType = "Long")
    private Long userId;
    @ApiModelProperty(notes = "微信OpenId", dataType = "String")
    private String openId;
    @ApiModelProperty(notes = "验证码", dataType = "String")
    private String captcha;
    @ApiModelProperty(notes = "纬度", dataType = "Double")
    private Double lat;
    @ApiModelProperty(notes = "经度", dataType = "Double")
    private Double lng;
    @ApiModelProperty(notes = "地址", dataType = "String")
    private String address;
    @ApiModelProperty(notes = "sessionId", dataType = "String")
    private String sessionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(notes = "登录时间", dataType = "Date")
    private Date loginAt = Date.from(Instant.now());
    @ApiModelProperty(notes = "登录类型", dataType = "LoginType")
    private LoginType loginType;
    @ApiModelProperty(notes = "应用类型", dataType = "Apply")
    private ApplicationType applicationType;
}
