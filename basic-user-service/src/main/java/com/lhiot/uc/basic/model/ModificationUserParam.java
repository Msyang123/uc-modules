package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng created in 2018/9/8 10:59
 **/
@Data
@ApiModel
public class ModificationUserParam {
    @ApiModelProperty(notes = "昵称", dataType = "String")
    private String nickname;
    @ApiModelProperty(notes = "头像", dataType = "String")
    private String avatar;
    @ApiModelProperty(notes = "邮箱", dataType = "String")
    private String email;
    @ApiModelProperty(notes = "QQ", dataType = "String")
    private String qq;
    @ApiModelProperty(notes = "地址", dataType = "String")
    private String address;
    @ApiModelProperty(notes = "个人说明", dataType = "String")
    private String description;
    @ApiModelProperty(notes = "性别", dataType = "String")
    private String sex;
    @ApiModelProperty(notes = "生日", dataType = "String")
    private String birthday;
}
