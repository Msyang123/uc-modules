package com.lhiot.uc.basic.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class DeviceUniqueResult {
    private Long id;
    private String ukey;
    private Long status;
    private Long userId;
    private Timestamp createAt;
}
