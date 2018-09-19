package com.lhiot.uc.basic.model;

import com.lhiot.uc.basic.entity.IfDefault;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author zhangfeng created in 2018/9/17 17:16
 **/
@Data
@ApiModel
public class UserAddress {
    private Long id;
    private Long baseUserId;
    private String deliveryName;
    private String deliveryDetail;
    private String deliveryMobile;
    private Double lat;
    private Double lng;
    private String deliverySex;
    private String streetDetail;
    private IfDefault ifDefault;
}
