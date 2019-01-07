package com.lhiot.uc.basic.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class ApplyUser {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String birthday;
    private String sex;
    private String phone;
    private String nickname;
    private String email;
    private String qq ;
    private String avatar;
    private String address;
    private String description;
    private Date registerAt = Date.from(Instant.now());
    private String password;
    private String paymentPassword;
    private String openId;
    private String unionId;
    private Long baseUserId;
    private LockStatus locked = LockStatus.UNLOCKED;
    private String applicationType;
    private SwitchStatus paymentPermissions = SwitchStatus.OPEN;

}
