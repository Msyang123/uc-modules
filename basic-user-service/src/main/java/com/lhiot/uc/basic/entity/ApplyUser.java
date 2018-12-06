package com.lhiot.uc.basic.entity;

import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
public class ApplyUser {

    private Long id;
    private String birthday = "";
    private String sex = "man";
    private String phone = "";
    private String nickname = "";
    private String email = "";
    private String qq = "";
    private String avatar = "http://resource.shuiguoshule.com.cn/user_image/2017-04-14/oGPg2MyfeUrO9knaDLyS.jpg";
    private String address = "";
    private String description = "";
    private Date registerAt = Date.from(Instant.now());
    private String password;
    private String paymentPassword;
    private String openId = "";
    private String unionId = "";
    private Long baseUserId;
    private LockStatus locked = LockStatus.UNLOCKED;
    private String applicationType;
    private SwitchStatus paymentPermissions = SwitchStatus.OPEN;

}
