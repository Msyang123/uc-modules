package com.lhiot.uc.basic.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author zhangfeng created in 2018/9/7 15:30
 **/
@Data
public class UserBinding {
    private Long id;
    private Long baseUserId;
    private Long applyUserId;
    private String phone;
    private Date createAt;
}
