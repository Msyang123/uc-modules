package com.lhiot.uc.basic.entity;

import com.lhiot.uc.basic.model.PhoneRegisterParam;
import lombok.Data;

@Data
public class BaseUser {

    private Long id;
    private String phone;
    private Long currency = 0L;
    private LockStatus locked = LockStatus.UNLOCKED;
    private SwitchStatus paymentPermissions = SwitchStatus.OPEN;
    private Long memberPoints = 0L;

    public BaseUser copy(Long id, PhoneRegisterParam param) {
        this.id = id;
        this.phone = param.getPhone();
//        BeanUtils.of(this).populate(param);
        return this;
    }
}
