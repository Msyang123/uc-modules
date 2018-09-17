package com.lhiot.uc.basic.entity;

import lombok.Data;

@Data
public class BaseUser {

    private Long id;
    private String phone;
    private Long balance = 0L;
    private LockStatus locked = LockStatus.UNLOCKED;
    private Long memberPoints = 0L;
    private String realName;
    private String idCard;
}
