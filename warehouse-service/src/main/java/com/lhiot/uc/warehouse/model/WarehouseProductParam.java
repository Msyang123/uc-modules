package com.lhiot.uc.warehouse.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@ApiModel
@Data
public class WarehouseProductParam {

    @ApiModelProperty(notes = "商品ID", dataType = "Long", required = true)
    @Min(1)
    private long productId;

    @ApiModelProperty(notes = "商品重量/数量", dataType = "BigDecimal", required = true)
    private BigDecimal productCount;
}
