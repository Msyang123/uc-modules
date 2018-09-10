package com.lhiot.uc.warehouse.api;

import com.lhiot.uc.warehouse.domain.entity.WarehouseGoodsExtract;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.service.WarehouseGoodsExtractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* Description:仓库商品提取接口类
* @author yijun
* @date 2018/09/07
*/
@Api(description = "仓库商品提取接口")
@Slf4j
@RestController
@RequestMapping("/warehouseGoodsExtract")
public class WarehouseGoodsExtractApi {

    private final WarehouseGoodsExtractService warehouseGoodsExtractService;

    @Autowired
    public WarehouseGoodsExtractApi(WarehouseGoodsExtractService warehouseGoodsExtractService) {
        this.warehouseGoodsExtractService = warehouseGoodsExtractService;
    }

/*    @PostMapping("/create")
    @ApiOperation(value = "添加仓库商品提取")
    @ApiImplicitParam(paramType = "body", name = "warehouseGoodsExtract", value = "要添加的仓库商品提取", required = true, dataType = "WarehouseGoodsExtract")
    public ResponseEntity<Integer> create(@RequestBody WarehouseGoodsExtract warehouseGoodsExtract) {
        log.debug("添加仓库商品提取\t param:{}",warehouseGoodsExtract);
        
        return ResponseEntity.ok(warehouseGoodsExtractService.create(warehouseGoodsExtract));
    }*/

    @PutMapping("/update/{id}")
    @ApiOperation(value = "根据id更新仓库商品提取")
    @ApiImplicitParam(paramType = "body", name = "warehouseGoodsExtract", value = "要更新的仓库商品提取", required = true, dataType = "WarehouseGoodsExtract")
    public ResponseEntity<Integer> update(@PathVariable("id") Long id,@RequestBody WarehouseGoodsExtract warehouseGoodsExtract) {
        log.debug("根据id更新仓库商品提取\t id:{} param:{}",id,warehouseGoodsExtract);
        warehouseGoodsExtract.setId(id);
        
        return ResponseEntity.ok(warehouseGoodsExtractService.updateById(warehouseGoodsExtract));
    }

/*    @DeleteMapping("/{ids}")
    @ApiOperation(value = "根据ids删除仓库商品提取")
    @ApiImplicitParam(paramType = "path", name = "ids", value = "要删除仓库商品提取的ids,逗号分割", required = true, dataType = "String")
    public ResponseEntity<Integer> deleteByIds(@PathVariable("ids") String ids) {
        log.debug("根据ids删除仓库商品提取\t param:{}",ids);
        
        return ResponseEntity.ok(warehouseGoodsExtractService.deleteByIds(ids));
    }*/
    
    @ApiOperation(value = "根据id查询仓库商品提取", notes = "根据id查询仓库商品提取")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseGoodsExtract> findWarehouseGoodsExtract(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseGoodsExtractService.selectById(id));
    }
    
    @GetMapping("/page/query")
    @ApiOperation(value = "查询仓库商品提取分页列表")
    public ResponseEntity<PagerResultObject<WarehouseGoodsExtract>> pageQuery(WarehouseGoodsExtract warehouseGoodsExtract){
        log.debug("查询仓库商品提取分页列表\t param:{}",warehouseGoodsExtract);
        
        return ResponseEntity.ok(warehouseGoodsExtractService.pageList(warehouseGoodsExtract));
    }
    
}
