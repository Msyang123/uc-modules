package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.Apply;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author zhangfeng created in 2018/9/7 17:12
 **/
@Data
@ApiModel
public class UserBindingPhoneParam {
    private String phone;
    private Long applyUserId;
    private Apply apply;
}
