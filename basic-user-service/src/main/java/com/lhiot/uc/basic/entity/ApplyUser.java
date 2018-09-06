package com.lhiot.uc.basic.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.basic.model.UserDetailResult;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.sql.Timestamp;

@Data
@ApiModel
public class ApplyUser {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String birthday;
    private String sex;
    private String phone;
    private String realname;
    private String nickname;
    private String email;
    private String qq;
    private String avatar;
    private String address;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp registrationAt;
    private String password;
    private String paymentPassword;
    private String userType;
    private String openId;
    private String unionId;
    private Long baseUserId;
    private Integer locked;
    private String apply;


    public ApplyUser copy(Long id, String phone){
        this.id = id;
        this.phone = phone;
        return this;
    }

    public ApplyUser copy(UserDetailResult param){
        BeanUtils.of(this).populate(param);
        return this;
    }
}
