package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangfeng create in 11:32 2018/10/10
 */
@Data
@ApiModel
public class SearchParam {
    @ApiModelProperty(notes = "用户ID集合组成的字符串逗号分隔",dataType = "String")
    private String ids;
    @ApiModelProperty(notes = "手机号集合组成的字符串逗号分隔",dataType = "String")
    private String phones;

    @ApiModelProperty(notes = "关键字",dataType = "String")
    private String keyword;
}
