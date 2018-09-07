package com.lhiot.uc.basic.entity;

import com.lhiot.uc.basic.model.PhoneRegisterParam;
import lombok.Data;

@Data
public class BaseUser {
    private Long id;
    private String phone;

    public BaseUser copy(Long id,PhoneRegisterParam param){
        this.id=id;
        this.phone=param.getPhone();
//        BeanUtils.of(this).populate(param);
        return this;
    }
}
