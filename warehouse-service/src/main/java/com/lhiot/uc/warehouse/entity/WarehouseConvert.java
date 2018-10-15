package com.lhiot.uc.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lhiot.uc.warehouse.model.ConvertType;
import com.lhiot.uc.warehouse.model.InOutType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Description:仓库出入库记录明细实体类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Data
@ToString(callSuper = true)
@ApiModel
@NoArgsConstructor
public class WarehouseConvert{

    /**
     * 仓库库存明细ID
     */
    @JsonProperty("id")
    @ApiModelProperty(value = "仓库库存明细ID", dataType = "Long")
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
     * 商品名称
     */
    @JsonProperty("productName")
    @ApiModelProperty(value = "商品名称", dataType = "String")
    private String productName;

    /**
     * 商品数量/重量
     */
    @JsonProperty("productCount")
    @ApiModelProperty(value = "商品数量/重量", dataType = "BigDecimal")
    private BigDecimal productCount;

    /**
     * 商品价格
     */
    @JsonProperty("price")
    @ApiModelProperty(value = "商品价格", dataType = "Integer")
    private Integer price;

    /**
     * 购买时间
     */
    @JsonProperty("buyAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "购买时间", dataType = "Date")
    private java.util.Date buyAt;


    /**
     * 出入库时间
     */
    @JsonProperty("convertAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "出入库时间", dataType = "Date")
    private java.util.Date convertAt;


    /**
     * 出入库标志in-入库 out-出库
     */
    @JsonProperty("inOut")
    @ApiModelProperty(value = "出入库标志in-入库 out-出库", dataType = "InOutType")
    private InOutType inOut;

    /**
     * 出入库原因
     */
    @JsonProperty("remark")
    @ApiModelProperty(value = "出入库原因", dataType = "String")
    private String remark;

    /**
     * 兑换类型：Auto-自动 Manual-手动
     */
    @JsonProperty("convertType")
    @ApiModelProperty(value = "兑换类型：Auto-自动Manual-手动", dataType = "ConvertType")
    private ConvertType convertType;

    /**
     * 兑换折扣
     */
    @JsonProperty("discount")
    @ApiModelProperty(value = "兑换折扣", dataType = "Integer")
    private Integer discount;

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
