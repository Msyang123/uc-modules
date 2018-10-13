package com.lhiot.uc.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhangfeng create in 15:02 2018/10/12
 */
@Data
@ApiModel
public class CountWarehouseProductResult {
    /**
     * 仓库商品ID
     */
    @ApiModelProperty(value = "仓库商品ID", dataType = "Long")
    private Long id;

    /**
     * 仓库id
     */
    @ApiModelProperty(value = "仓库id", dataType = "Long")
    private Long warehouseId;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id", dataType = "Long")
    private Long productId;

    /**
     * 商品数量/重量
     */
    @ApiModelProperty(value = "单个商品总（数量/重量）", dataType = "BigDecimal")
    private BigDecimal totalCount;

    /**
     * 是否允许拆分
     */
    @ApiModelProperty(value = "是否允许拆分", dataType = "IfSplitType")
    private IfSplitType ifSplit;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "单个商品总价值", dataType = "Integer")
    private Integer totalPrice;

    /**
     * 基础商品条码
     */
    @ApiModelProperty(value = "基础商品条码", dataType = "String")
    private String barcode;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String productName;

    /**
     * 商品小图标
     */
    @JsonProperty("smallImage")
    @ApiModelProperty(value = "商品小图标", dataType = "String")
    private String smallImage;
}
