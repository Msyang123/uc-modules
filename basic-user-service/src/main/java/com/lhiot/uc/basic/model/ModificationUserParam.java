package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.SwitchStatus;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author zhangfeng created in 2018/9/8 10:59
 **/
@Data
@ApiModel
public class ModificationUserParam {
    private String nickname;
    private String avatar;
    private String email;
    private String qq;
    private String address;
    private String description;
    private String sex;
    private String birthday;
}
