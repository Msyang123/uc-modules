package com.lhiot.uc.warehouse.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description:用户仓库实体类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Data
@ToString(callSuper = true)
@ApiModel
@NoArgsConstructor
public class WarehouseUser{

    /**
     * 仓库id
     */
    @JsonProperty("id")
    @ApiModelProperty(value = "仓库id", dataType = "Long")
    private Long id;

    /**
     * 基础用户id
     */
    @JsonProperty("baseUserId")
    @ApiModelProperty(value = "基础用户id", dataType = "Long")
    private Long baseUserId;

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
