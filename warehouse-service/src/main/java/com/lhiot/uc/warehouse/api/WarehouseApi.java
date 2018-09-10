package com.lhiot.uc.warehouse.api;

import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.service.WarehouseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
* Description:用户仓库接口类
* @author yijun
* @date 2018/09/07
*/
@Api(description = "用户仓库接口")
@Slf4j
@RestController
@RequestMapping("/warehouse")
public class WarehouseApi {

    private final WarehouseUserService warehouseUserService;


    @Autowired
    public WarehouseApi(WarehouseUserService warehouseUserService) {
        this.warehouseUserService = warehouseUserService;
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加用户仓库")
    @ApiImplicitParam(paramType = "body", name = "warehouseUser", value = "要添加的用户仓库", required = true, dataType = "WarehouseUser")
    public ResponseEntity<Integer> add(@RequestBody WarehouseUser warehouseUser) {
        log.debug("添加用户仓库\t param:{}",warehouseUser);
        
        return ResponseEntity.ok(warehouseUserService.add(warehouseUser));
    }

    @PutMapping("/update/{id}")
    @ApiOperation(value = "根据id更新用户仓库")
    @ApiImplicitParam(paramType = "body", name = "warehouseUser", value = "要更新的用户仓库", required = true, dataType = "WarehouseUser")
    public ResponseEntity<Integer> update(@PathVariable("id") Long id,@RequestBody WarehouseUser warehouseUser) {
        log.debug("根据id更新用户仓库\t id:{} param:{}",id,warehouseUser);
        warehouseUser.setId(id);
        
        return ResponseEntity.ok(warehouseUserService.updateById(warehouseUser));
    }
    
    @ApiOperation(value = "根据id查询用户仓库", notes = "根据id查询用户仓库")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseUser> findWarehouseUser(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseUserService.selectById(id));
    }
    
    @GetMapping("/page/select")
    @ApiOperation(value = "查询用户仓库分页列表")
    public ResponseEntity<PagerResultObject<WarehouseUser>> pageSelect(WarehouseUser warehouseUser){
        log.debug("查询用户仓库分页列表\t param:{}",warehouseUser);
        
        return ResponseEntity.ok(warehouseUserService.pageList(warehouseUser));
    }
    
}
