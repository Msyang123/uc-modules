package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.Apply;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class WeChatRegisterParam {
    @ApiModelProperty(notes = "手机号",dataType = "String")
    private String phone;
    @ApiModelProperty(notes = "微信openId",dataType = "String")
    private String openId;
    @ApiModelProperty(notes = "unionId",dataType = "String")
    private String unionId;
    @ApiModelProperty(notes = "昵称",dataType = "String")
    private String nickname;
    @ApiModelProperty(notes = "性别",dataType = "String")
    private String sex;
    @ApiModelProperty(notes = "地址",dataType = "String")
    private String address;
    @ApiModelProperty(notes = "个人说明",dataType = "String")
    private String description;
    @ApiModelProperty(notes = "头像",dataType = "String")
    private String avatar;
    @ApiModelProperty(notes = "应用类型",dataType = "Apply")
    private Apply apply;
}
