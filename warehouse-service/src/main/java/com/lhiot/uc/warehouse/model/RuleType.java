package com.lhiot.uc.warehouse.model;

/**
 * @author zhangfeng create in 11:25 2018/10/15
 */
public enum  RuleType {
    WAREHOUSE_RULE("rule","warehouse-convert-rule")
    ;

    public String getCode() {
        return code;
    }

    public String getDictCode() {
        return dictCode;
    }

    private String code;
    private String dictCode;
    RuleType(String code,String cgCode){
        this.code = code;
        this.dictCode = cgCode;
    }
}
