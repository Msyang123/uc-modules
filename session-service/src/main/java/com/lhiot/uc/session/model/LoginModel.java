package com.lhiot.uc.session.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Leon (234239150@qq.com) created in 9:36 18.8.30
 */
@Data
@ApiModel
public class LoginModel {

    private String phoneNumber;

    private String password;

    private String captcha;

    private LoginType type;

    public enum LoginType {
        PWD, SMS
    }
}
