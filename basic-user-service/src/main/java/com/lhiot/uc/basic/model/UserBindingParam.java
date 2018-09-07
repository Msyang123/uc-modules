package com.lhiot.uc.basic.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author zhangfeng created in 2018/9/7 17:12
 **/
@Data
@ApiModel
public class UserBindingParam {
    private String phone;
    private Long applyUserId;
}
