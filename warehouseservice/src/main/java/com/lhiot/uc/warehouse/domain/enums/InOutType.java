package com.lhiot.uc.warehouse.domain.enums;

import lombok.Getter;

/**
 * 仓库兑换类型
 */
public enum InOutType {
    IN("入库"),
    OUT("出库");
    @Getter
    private String description;

    InOutType(String description) {
        this.description = description;
    }
}
