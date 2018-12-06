package com.lhiot.uc.basic.model;

import com.lhiot.dc.dictionary.HasEntries;
import com.lhiot.uc.basic.entity.ApplicationType;
import lombok.Data;

/**
 * @author zhangfeng create in 14:14 2018/10/10
 */
@Data
public class PhoneAndPasswordSearchParam {
    private String phone;
    private String password;
    @HasEntries(from = "applications",message = "该字典项不存在！")
    private String applicationType;
}
