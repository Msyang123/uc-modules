package com.lhiot.uc.session.model;

import lombok.Data;

/**
 * @author zhangfeng create in 14:26 2018/10/10
 */
@Data
public class SearchParam {
    private String phone;
    private String password;
    private ApplicationType applicationType;
}
