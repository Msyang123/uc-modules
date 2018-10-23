package com.lhiot.uc.warehouse.api;

import com.lhiot.uc.warehouse.service.WarehouseOverdueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:仓库商品过期降价值处理接口类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Api(description = "仓库商品过期降价值处理接口")
@Slf4j
@RestController
@RequestMapping("/warehouse/product-overdue")
//TODO 取消定时过期，打开仓库时触发
public class WarehouseOverdueApi {

    private final WarehouseOverdueService warehouseOverdueService;

    @Autowired
    public WarehouseOverdueApi(WarehouseOverdueService warehouseOverdueService) {
        this.warehouseOverdueService = warehouseOverdueService;
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "根据ids删除仓库商品过期降价值处理")
    @ApiImplicitParam(paramType = "path", name = "ids", value = "要删除仓库商品过期降价值处理的ids,逗号分割", required = true, dataType = "String")
    public ResponseEntity<Integer> deleteByIds(@PathVariable("ids") String ids) {
        log.debug("根据ids删除仓库商品过期降价值处理\t param:{}", ids);

        return ResponseEntity.ok(warehouseOverdueService.deleteByIds(ids));
    }
}
