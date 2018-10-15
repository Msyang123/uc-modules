package com.lhiot.uc.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhiot.uc.warehouse.model.IfSplitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description:仓库商品提取实体类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Data
@ToString(callSuper = true)
@ApiModel
@NoArgsConstructor
public class WarehouseProductExtract{

    /**
     * 仓库商品ID
     */
    @JsonProperty("id")
    @ApiModelProperty(value = "仓库商品ID", dataType = "Long")
    private Long id;

    /**
     * 仓库id
     */
    @JsonProperty("warehouseId")
    @ApiModelProperty(value = "仓库id", dataType = "Long")
    private Long warehouseId;

    /**
     * 商品id
     */
    @JsonProperty("productId")
    @ApiModelProperty(value = "商品id", dataType = "Long")
    private Long productId;

    /**
     * 商品数量/重量
     */
    @JsonProperty("productCount")
    @ApiModelProperty(value = "商品数量/重量", dataType = "Double")
    private Double productCount;

    /**
     * 是否允许拆分
     */
    @JsonProperty("ifSplit")
    @ApiModelProperty(value = "是否允许拆分", dataType = "IfSplitType")
    private IfSplitType ifSplit;

    /**
     * 商品价格
     */
    @JsonProperty("price")
    @ApiModelProperty(value = "商品价格", dataType = "Integer")
    private Integer price;

    /**
     * 基础商品条码
     */
    @JsonProperty("barcode")
    @ApiModelProperty(value = "基础商品条码", dataType = "String")
    private String barcode;

    /**
     * 商品名称
     */
    @JsonProperty("productName")
    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String productName;

    /**
     * 商品主图
     */
    @JsonProperty("image")
    @ApiModelProperty(value = "商品主图", dataType = "String")
    private String image;

    /**
     * 商品小图标
     */
    @JsonProperty("smallImage")
    @ApiModelProperty(value = "商品小图标", dataType = "String")
    private String smallImage;

    /**
     * 大图标
     */
    @JsonProperty("largeImage")
    @ApiModelProperty(value = "大图标", dataType = "String")
    private String largeImage;

    /**
     * 购买时间
     */
    @JsonProperty("buyAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "购买时间", dataType = "Date")
    private java.util.Date buyAt;


    /**
     * 创建时间
     */
    @JsonProperty("createAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", dataType = "Date")
    private java.util.Date createAt;


    /**
     * 提取的订单编号
     */
    @JsonProperty("orderCode")
    @ApiModelProperty(value = "提取的订单编号", dataType = "String")
    private String orderCode;

    @JsonIgnore
    @ApiModelProperty(value = "当前页,默认值1")
    private Long page = 1L;

    /**
     * 传入-1可不分页
     */
    @JsonIgnore
    @ApiModelProperty(value = "每页显示条数,默认值10")
    private Long rows = 10L;

}
