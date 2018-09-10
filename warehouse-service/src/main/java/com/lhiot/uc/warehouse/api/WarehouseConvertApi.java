package com.lhiot.uc.warehouse.api;

import com.lhiot.uc.warehouse.domain.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.service.WarehouseConvertService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* Description:仓库出入库记录明细接口类
* @author yijun
* @date 2018/09/07
*/
@Api(description = "仓库出入库记录明细接口")
@Slf4j
@RestController
@RequestMapping("/warehouseConvert")
public class WarehouseConvertApi {

    private final WarehouseConvertService warehouseConvertService;

    @Autowired
    public WarehouseConvertApi(WarehouseConvertService warehouseConvertService) {
        this.warehouseConvertService = warehouseConvertService;
    }
    @PutMapping("/update/{id}")
    @ApiOperation(value = "根据id更新仓库出入库记录明细")
    @ApiImplicitParam(paramType = "body", name = "warehouseConvert", value = "要更新的仓库出入库记录明细", required = true, dataType = "WarehouseConvert")
    public ResponseEntity<Integer> update(@PathVariable("id") Long id,@RequestBody WarehouseConvert warehouseConvert) {
        log.debug("根据id更新仓库出入库记录明细\t id:{} param:{}",id,warehouseConvert);
        warehouseConvert.setId(id);
        
        return ResponseEntity.ok(warehouseConvertService.updateById(warehouseConvert));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "根据ids删除仓库出入库记录明细")
    @ApiImplicitParam(paramType = "path", name = "ids", value = "要删除仓库出入库记录明细的ids,逗号分割", required = true, dataType = "String")
    public ResponseEntity<Integer> deleteByIds(@PathVariable("ids") String ids) {
        log.debug("根据ids删除仓库出入库记录明细\t param:{}",ids);
        
        return ResponseEntity.ok(warehouseConvertService.deleteByIds(ids));
    }
    
    @ApiOperation(value = "根据id查询仓库出入库记录明细", notes = "根据id查询仓库出入库记录明细")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseConvert> findWarehouseConvert(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseConvertService.selectById(id));
    }
    
    @GetMapping("/page/query")
    @ApiOperation(value = "查询仓库出入库记录明细分页列表")
    public ResponseEntity<PagerResultObject<WarehouseConvert>> pageQuery(WarehouseConvert warehouseConvert){
        log.debug("查询仓库出入库记录明细分页列表\t param:{}",warehouseConvert);
        
        return ResponseEntity.ok(warehouseConvertService.pageList(warehouseConvert));
    }
    
}
