package com.lhiot.uc.warehouse.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhiot.uc.warehouse.domain.common.PagerRequestObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
* Description:仓库商品过期降价值处理实体类
* @author yijun
* @date 2018/09/07
*/
@Data
@ToString(callSuper = true)
@ApiModel
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarehouseOverdue extends PagerRequestObject {

    /**
    *过期ID
    */
    @JsonProperty("id")
    @ApiModelProperty(value = "过期ID", dataType = "Long")
    private Long id;

    /**
    *商品id
    */
    @JsonProperty("goodsId")
    @ApiModelProperty(value = "商品id", dataType = "Long")
    private Long goodsId;

    /**
    *商品名称
    */
    @JsonProperty("productName")
    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String productName;

    /**
    *商品数量
    */
    @JsonProperty("goodsCount")
    @ApiModelProperty(value = "商品数量", dataType = "BigDecimal")
    private BigDecimal goodsCount;

    /**
    *商品价格
    */
    @JsonProperty("price")
    @ApiModelProperty(value = "商品价格", dataType = "Integer")
    private Integer price;

    /**
    *购买时间
    */
    @JsonProperty("buyAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty(value = "购买时间", dataType = "Date")
    private java.util.Date buyAt;


    /**
    *仓库Id
    */
    @JsonProperty("warehouseId")
    @ApiModelProperty(value = "仓库Id", dataType = "Long")
    private Long warehouseId;

}
