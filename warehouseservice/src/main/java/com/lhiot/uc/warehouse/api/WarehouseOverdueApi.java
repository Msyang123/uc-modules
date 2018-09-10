package com.lhiot.uc.warehouse.api;

import com.lhiot.uc.warehouse.domain.entity.WarehouseOverdue;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.service.WarehouseOverdueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* Description:仓库商品过期降价值处理接口类
* @author yijun
* @date 2018/09/07
*/
@Api(description = "仓库商品过期降价值处理接口")
@Slf4j
@RestController
@RequestMapping("/warehouseOverdue")
public class WarehouseOverdueApi {

    private final WarehouseOverdueService warehouseOverdueService;

    @Autowired
    public WarehouseOverdueApi(WarehouseOverdueService warehouseOverdueService) {
        this.warehouseOverdueService = warehouseOverdueService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "添加仓库商品过期降价值处理")
    @ApiImplicitParam(paramType = "body", name = "warehouseOverdue", value = "要添加的仓库商品过期降价值处理", required = true, dataType = "WarehouseOverdue")
    public ResponseEntity<Integer> create(@RequestBody WarehouseOverdue warehouseOverdue) {
        log.debug("添加仓库商品过期降价值处理\t param:{}",warehouseOverdue);
        
        return ResponseEntity.ok(warehouseOverdueService.create(warehouseOverdue));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "根据id更新仓库商品过期降价值处理")
    @ApiImplicitParam(paramType = "body", name = "warehouseOverdue", value = "要更新的仓库商品过期降价值处理", required = true, dataType = "WarehouseOverdue")
    public ResponseEntity<Integer> update(@PathVariable("id") Long id,@RequestBody WarehouseOverdue warehouseOverdue) {
        log.debug("根据id更新仓库商品过期降价值处理\t id:{} param:{}",id,warehouseOverdue);
        warehouseOverdue.setId(id);
        
        return ResponseEntity.ok(warehouseOverdueService.updateById(warehouseOverdue));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "根据ids删除仓库商品过期降价值处理")
    @ApiImplicitParam(paramType = "path", name = "ids", value = "要删除仓库商品过期降价值处理的ids,逗号分割", required = true, dataType = "String")
    public ResponseEntity<Integer> deleteByIds(@PathVariable("ids") String ids) {
        log.debug("根据ids删除仓库商品过期降价值处理\t param:{}",ids);
        
        return ResponseEntity.ok(warehouseOverdueService.deleteByIds(ids));
    }
    
    @ApiOperation(value = "根据id查询仓库商品过期降价值处理", notes = "根据id查询仓库商品过期降价值处理")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseOverdue> findWarehouseOverdue(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseOverdueService.selectById(id));
    }
    
    @GetMapping("/page/query")
    @ApiOperation(value = "查询仓库商品过期降价值处理分页列表")
    public ResponseEntity<PagerResultObject<WarehouseOverdue>> pageQuery(WarehouseOverdue warehouseOverdue){
        log.debug("查询仓库商品过期降价值处理分页列表\t param:{}",warehouseOverdue);
        
        return ResponseEntity.ok(warehouseOverdueService.pageList(warehouseOverdue));
    }
    
}
