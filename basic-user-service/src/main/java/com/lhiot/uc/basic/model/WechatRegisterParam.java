package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.Apply;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class WechatRegisterParam {
    private String openId;
    private String unionId;
    private String nickname;
    private String sex;
    private String address;
    private String description;
    private String avatar;
    private Apply apply;
}
