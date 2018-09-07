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

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(notes = "生日", dataType = "String")
    private String birthday = "";

    @ApiModelProperty(notes = "性别:man(男),women(女),privary(保密)", dataType = "String")
    private String sex = "man";

    @ApiModelProperty(notes = "手机号", dataType = "String")
    private String phone = "";

    @ApiModelProperty(notes = "真名", dataType = "String")
    private String realname = "";

    @ApiModelProperty(notes = "昵称", dataType = "String")
    private String nickname = "";

    @ApiModelProperty(notes = "邮箱", dataType = "String")
    private String email = "";

    @ApiModelProperty(notes = "QQ", dataType = "String")
    private String qq = "";

    @ApiModelProperty(notes = "头像", dataType = "String")
    private String avatar = "http://resource.shuiguoshule.com.cn/user_image/2017-04-14/oGPg2MyfeUrO9knaDLyS.jpg";

    @ApiModelProperty(notes = "地址", dataType = "String")
    private String address = "";

    @ApiModelProperty(notes = "备注", dataType = "String")
    private String description = "";

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(notes = "注册时间", dataType = "Timestamp")
    private Timestamp registrationAt;

    @ApiModelProperty(notes = "积分", dataType = "Integer")
    private Integer point = 0;

    @ApiModelProperty(notes = "鲜果币", dataType = "Long")
    private Long currency = 0L;

    @ApiModelProperty(notes = "等级", dataType = "String")
    private String level = "1";

    @ApiModelProperty(notes="密码",dataType="String")
    private String password = "";

    @ApiModelProperty(notes="支付密码",dataType="String")
    private String paymentPassword = "";

    @ApiModelProperty(notes="微信开放平台单一应用唯一识别openId",dataType="String")
    private String openId ="";

    @ApiModelProperty(notes="同一个微信开放平台多个应用唯一识别unionId",dataType="String")
    private String unionId = "";

    @ApiModelProperty(notes="t_base_user的id",dataType="Long")
    private Long baseUserId ;

    @ApiModelProperty(notes="是否锁定0未锁定1锁定",dataType="Integer")
    private String locked = "lock";

    @ApiModelProperty(notes = "应用类型",dataType = "String")
    private String apply;
}
