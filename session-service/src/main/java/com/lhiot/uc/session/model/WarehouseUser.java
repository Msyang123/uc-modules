package com.lhiot.uc.session.model;

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
public class WarehouseUser {

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
}
