package com.lhiot.uc.warehouse.domain.enums;

import lombok.Getter;

/**
 * 仓库商品操作类型
 */
public enum OperType {
    UPDATE("更新"),
    REMOVE("删除");
    @Getter
    private String description;

    OperType(String description) {
        this.description = description;
    }
}
