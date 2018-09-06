package com.lhiot.uc.basic.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
public class UserExtension {
    private Long id;
    private Long userId;
    private String inviteCode;
    private String level;
    private String locked;
    private String locationShare;
    private String informationShare;
    private String paymentPermissions;
}
