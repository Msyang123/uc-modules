package com.lhiot.uc.warehouse.api;

import com.leon.microx.support.result.Pages;
import com.leon.microx.support.swagger.ApiHideBodyProperty;
import com.lhiot.uc.warehouse.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.entity.WarehouseUser;
import com.lhiot.uc.warehouse.model.WarehouseConvertParam;
import com.lhiot.uc.warehouse.service.WarehouseConvertService;
import com.lhiot.uc.warehouse.service.WarehouseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:用户仓库接口类
 *
 * @author yijun
 */
@Api(description = "用户仓库接口")
@Slf4j
@RestController
@RequestMapping("/warehouse")
public class WarehouseApi {

    private final WarehouseUserService warehouseUserService;
    private final WarehouseConvertService warehouseConvertService;


    @Autowired
    public WarehouseApi(WarehouseUserService warehouseUserService, WarehouseConvertService warehouseConvertService) {
        this.warehouseUserService = warehouseUserService;
        this.warehouseConvertService = warehouseConvertService;
    }

    @PostMapping({"", "/"})
    @ApiOperation(value = "添加用户仓库")
    @ApiImplicitParam(paramType = "body", name = "warehouseUser", value = "要添加的用户仓库", required = true, dataType = "WarehouseUser")
    public ResponseEntity<Integer> add(@RequestBody WarehouseUser warehouseUser) {
        log.debug("添加用户仓库\t param:{}", warehouseUser);

        return ResponseEntity.ok(warehouseUserService.add(warehouseUser));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "根据id更新用户仓库")
    @ApiImplicitParam(paramType = "body", name = "warehouseUser", value = "要更新的用户仓库", required = true, dataType = "WarehouseUser")
    public ResponseEntity<Integer> update(@PathVariable("id") Long id, @RequestBody WarehouseUser warehouseUser) {
        log.debug("根据id更新用户仓库\t id:{} param:{}", id, warehouseUser);
        warehouseUser.setId(id);

        return ResponseEntity.ok(warehouseUserService.updateById(warehouseUser));
    }

    @ApiOperation(value = "根据id查询用户仓库", notes = "根据id查询用户仓库")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseUser> findWarehouseUser(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseUserService.selectById(id));
    }

    @ApiOperation(value = "根据基础用户id查询用户仓库", notes = "根据基础用户id查询用户仓库")
    @ApiImplicitParam(paramType = "path", name = "baseUserId", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/base-users/{baseUserId}")
    public ResponseEntity<WarehouseUser> findWarehouseUserByBaseUserId(@PathVariable("baseUserId") Long baseUserId) {

        return ResponseEntity.ok(warehouseUserService.findByBaseUserId(baseUserId));
    }

    @PostMapping("/pages")
    @ApiOperation(value = "查询用户仓库分页列表")
    public ResponseEntity<Pages<WarehouseUser>> pages(@RequestBody WarehouseUser warehouseUser) {
        log.debug("查询用户仓库分页列表\t param:{}", warehouseUser);
        return ResponseEntity.ok(warehouseUserService.warehouseUserPageList(warehouseUser));
    }

    @PostMapping("/search")
    @ApiOperation(value = "查询用户仓库列表")
    public ResponseEntity<List<WarehouseUser>> warehouseUserList(@RequestBody WarehouseUser warehouseUser) {
        log.debug("查询用户仓库列表\t param:{}", warehouseUser);
        return ResponseEntity.ok(warehouseUserService.warehouseUserList(warehouseUser));
    }

    @PostMapping("/{warehouseId}/in-out-list")
    @ApiOperation(value = "根据仓库Id查询仓库出入库记录明细分页列表")
    @ApiHideBodyProperty({"startRow", "warehouseId"})
    public ResponseEntity<Pages<WarehouseConvert>> pageSelect(@PathVariable("warehouseId") Long warehouseId, @RequestBody WarehouseConvertParam param) {
        log.debug("查询仓库出入库记录明细分页列表\t param:{}", param);
        param.setWarehouseId(warehouseId);
        param.setStartRow((param.getPage() - 1) * param.getRows());
        return ResponseEntity.ok(warehouseConvertService.pageList(param));
    }

}
