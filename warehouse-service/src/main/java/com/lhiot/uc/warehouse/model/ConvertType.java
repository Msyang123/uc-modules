package com.lhiot.uc.warehouse.model;

import lombok.Getter;

/**
 * 仓库兑换类型
 */
public enum ConvertType {
    AUTO("自动"),
    MANUAL("手动");
    @Getter
    private String description;

    ConvertType(String description) {
        this.description = description;
    }
}
