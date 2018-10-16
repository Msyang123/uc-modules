package com.lhiot.uc.warehouse.conversion;

/**
 * @author zhangfeng create in 11:25 2018/10/15
 */
public enum  RuleType {
    WAREHOUSE_RULE("rule","warehouse-convert-rule")
    ;

    public String getCode() {
        return code;
    }

    public String getCgCode() {
        return cgCode;
    }

    private String code;
    private String cgCode;
    RuleType(String code,String cgCode){
        this.code = code;
        this.cgCode = cgCode;
    }
}
