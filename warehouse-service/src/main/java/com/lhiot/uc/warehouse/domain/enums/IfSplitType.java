package com.lhiot.uc.warehouse.domain.enums;

import lombok.Getter;

/**
 * 是否允许拆分
 */
public enum IfSplitType {
    YES("允许"),
    NO("不允许");
    @Getter
    private String description;

    IfSplitType(String description) {
        this.description = description;
    }
}
