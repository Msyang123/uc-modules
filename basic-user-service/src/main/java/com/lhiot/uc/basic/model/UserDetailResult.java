package com.lhiot.uc.basic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
@ApiModel
public class UserDetailResult {

    @JsonProperty("userId")
    @ApiModelProperty(notes = "ID", dataType = "Long")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonProperty("userBirthday")
    @ApiModelProperty(notes = "生日", dataType = "String")
    private String birthday;

    @JsonProperty("userSex")
    @ApiModelProperty(notes = "性别:man(男),women(女),privary(保密)", dataType = "String")
    private String sex;

    @JsonProperty("userMobile")
    @ApiModelProperty(notes = "手机号", dataType = "String")
    private String phone;

    @JsonProperty("userRealname")
    @ApiModelProperty(notes = "真名", dataType = "String")
    private String realname;

    @JsonProperty("userNickname")
    @ApiModelProperty(notes = "昵称", dataType = "String")
    private String nickname;

    @JsonProperty("userEmail")
    @ApiModelProperty(notes = "邮箱", dataType = "String")
    private String email;

    @JsonProperty("userQq")
    @ApiModelProperty(notes = "QQ", dataType = "String")
    private String qq;

    @JsonProperty("userIcon")
    @ApiModelProperty(notes = "头像", dataType = "String")
    private String avatar;

    @JsonProperty("userAddress")
    @ApiModelProperty(notes = "地址", dataType = "String")
    private String address;

    @JsonProperty("userDesc")
    @ApiModelProperty(notes = "备注", dataType = "String")
    private String description;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonProperty("registrationTime")
    @ApiModelProperty(notes = "注册时间", dataType = "Timestamp")
    private Timestamp registrationAt;

    @JsonProperty("userPoint")
    @ApiModelProperty(notes = "积分", dataType = "Integer")
    private Integer point;

    @JsonProperty("userCurrency")
    @ApiModelProperty(notes = "鲜果币", dataType = "Integer")
    private Integer currency;

    @JsonProperty("userLevel")
    @ApiModelProperty(notes = "等级", dataType = "String")
    private String level;

    @JsonProperty("password")
    @ApiModelProperty(notes="密码",dataType="String")
    private String password;

    @JsonProperty("paymentPassword")
    @ApiModelProperty(notes="支付密码",dataType="String")
    private String paymentPassword;


    @JsonProperty("userType")
    @ApiModelProperty(notes="用户类型：0-官方，1-非官方",dataType="String")
    private String userType;

    @JsonProperty("open_id")
    @ApiModelProperty(notes="微信开放平台单一应用唯一识别openId",dataType="String")
    private String openId;

    @JsonProperty("union_id")
    @ApiModelProperty(notes="同一个微信开放平台多个应用唯一识别unionId",dataType="String")
    private String unionId;

    @ApiModelProperty(notes="t_base_user的id",dataType="Long")
    private Long baseUserId;

    @ApiModelProperty(notes="是否锁定0未锁定1锁定",dataType="Integer")
    private Integer locked;

    @ApiModelProperty(notes = "应用类型",dataType = "String")
    private String apply;
}
