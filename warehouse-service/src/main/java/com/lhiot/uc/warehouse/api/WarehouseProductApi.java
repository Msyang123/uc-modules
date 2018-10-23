package com.lhiot.uc.warehouse.api;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Maps;
import com.leon.microx.util.Pair;
import com.leon.microx.web.result.Multiple;
import com.leon.microx.web.swagger.ApiParamType;
import com.lhiot.uc.warehouse.aspect.WarehouseProductConvert;
import com.lhiot.uc.warehouse.entity.WarehouseProduct;
import com.lhiot.uc.warehouse.entity.WarehouseProductExtract;
import com.lhiot.uc.warehouse.entity.WarehouseUser;
import com.lhiot.uc.warehouse.feign.BasicDataService;
import com.lhiot.uc.warehouse.mapper.WarehouseProductMapper;
import com.lhiot.uc.warehouse.model.CountWarehouseProductResult;
import com.lhiot.uc.warehouse.model.WarehouseProductParam;
import com.lhiot.uc.warehouse.service.WarehouseProductExtractService;
import com.lhiot.uc.warehouse.service.WarehouseProductService;
import com.lhiot.uc.warehouse.service.WarehouseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Description:仓库商品接口类
 *
 * @author yijun
 */
@Api(description = "仓库商品接口")
@Slf4j
@RestController
public class WarehouseProductApi {

    private final WarehouseUserService warehouseUserService;
    private final WarehouseProductService warehouseProductService;
    private final WarehouseProductExtractService warehouseProductExtractService;
    private WarehouseProductMapper warehouseProductMapper;
    private final BasicDataService basicDataService;

    @Autowired
    public WarehouseProductApi(WarehouseUserService warehouseUserService, WarehouseProductService warehouseProductService, WarehouseProductExtractService warehouseProductExtractService, WarehouseProductMapper warehouseProductMapper, BasicDataService basicDataService) {
        this.warehouseUserService = warehouseUserService;
        this.warehouseProductService = warehouseProductService;
        this.warehouseProductExtractService = warehouseProductExtractService;
        this.warehouseProductMapper = warehouseProductMapper;
        this.basicDataService = basicDataService;
    }


    @ApiOperation(value = "根据id查询仓库商品", notes = "根据id查询仓库商品")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/warehouse/products/{id}")
    public ResponseEntity<WarehouseProduct> findWarehouseProduct(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseProductService.selectById(id));
    }

    @ApiOperation(value = "根据仓库Id查询仓库商品列表", response = CountWarehouseProductResult.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = ApiParamType.PATH, name = "warehouseId", value = "仓库id", required = true, dataType = "long")
    @GetMapping("/warehouse/{warehouseId}/products")
    @WarehouseProductConvert(warehouseId = "#warehouseId")
    public ResponseEntity getWhProduct(@PathVariable("warehouseId") Long warehouseId) {

        if (Objects.isNull(warehouseId)) {
            ResponseEntity.badRequest().body("传递参数错误");
        }
        WarehouseProduct warehouseProduct = new WarehouseProduct();
        warehouseProduct.setWarehouseId(warehouseId);
        List<CountWarehouseProductResult> resultList = warehouseProductMapper.sumProductByWarehouseId(warehouseId);
        return ResponseEntity.ok(Multiple.of(resultList));
    }

    @ApiOperation(value = "根据仓库ID和商品Id查询商品", response = CountWarehouseProductResult.class, responseContainer = "Set")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = ApiParamType.PATH, name = "warehouseId", value = "仓库Id", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = ApiParamType.PATH, name = "productId", value = "商品Id", dataType = "Long", required = true)
    })
    @GetMapping("/warehouse/{warehouseId}/products/{productId}")
    public ResponseEntity findProductByIdInWarehouse(@PathVariable("warehouseId") Long warehouseId, @PathVariable("productId") Long productId) {
        List<WarehouseProduct> productList = warehouseProductMapper.findProductByIdInWarehouse(Maps.of("warehouseId", warehouseId, "productId", productId));
        if (CollectionUtils.isEmpty(productList)) {
            productList = new ArrayList<>();
        }
        return ResponseEntity.ok(Multiple.of(productList));
    }


    /*************************用户仓库存储与提取******************************************************/
    @ApiOperation("存入用户仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "warehouseId", value = "仓库Id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "body", name = "warehouseProductParamList", dataType = "WarehouseProductParam", dataTypeClass = WarehouseProductParam.class,
                    allowMultiple = true, value = "存入仓库商品列表", required = true),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String")
    })
    @PostMapping("/warehouse/products/in")
    @WarehouseProductConvert(warehouseId = "#warehouseId")
    public ResponseEntity<?> inWarehouse(
            @RequestParam @NotNull @Min(1) Long warehouseId,
            @RequestBody List<WarehouseProductParam> warehouseProductParamList,
            @RequestParam String remark) {
        if (Objects.isNull(warehouseProductParamList) || warehouseProductParamList.isEmpty()) {
            return ResponseEntity.badRequest().body("存入仓库商品列表为空");
        }
        //查找用户仓库
        WarehouseUser warehouseUser = warehouseUserService.selectById(warehouseId);
        if (Objects.isNull(warehouseUser)) {
            return ResponseEntity.badRequest().body("未找到用户仓库");
        }
        //TODO 需要查询基础商品信息构造仓库商品信息
//        basicDataService.findProductBySpecificationId()
        List<WarehouseProduct> warehouseProductList = new ArrayList<>(warehouseProductParamList.size());
        warehouseProductParamList.forEach(item -> {
            WarehouseProduct warehouseProduct = new WarehouseProduct();
            warehouseProduct.setWarehouseId(warehouseUser.getId());
            BeanUtils.of(warehouseProduct).populate(item);
        });

        boolean result = warehouseProductService.addWarehouseProduct(warehouseProductList, warehouseId, remark);
        if (result) {
            return ResponseEntity.ok("存入成功");
        } else {
            return ResponseEntity.badRequest().body("存入用户仓库失败");
        }
    }

    @ApiOperation("直接转出用户仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "warehouseId", value = "仓库Id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "orderCode", value = "订单编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "body", name = "warehouseProductParamList", dataType = "WarehouseProductParam", dataTypeClass = WarehouseProductParam.class,
                    allowMultiple = true, value = "转出申请商品列表", required = true)
    })
    @PostMapping("/warehouse/products/out")
    @WarehouseProductConvert(warehouseId = "#warehouseId")
    public ResponseEntity<?> direct(@RequestParam @NotNull @Min(1) Long warehouseId,
                                    @RequestParam @NotNull String orderCode,
                                    @RequestParam String remark,
                                    @RequestBody List<WarehouseProductParam> warehouseProductParamList) {
        //校验用户仓库商品是否足够提取申请 如果足够将商品转入暂存表中等待提取

        Pair<Integer, List<WarehouseProduct>> pair = warehouseProductService.waitExtractWarehouseProduct(warehouseId, warehouseProductParamList);
        if (Objects.isNull(pair)) {
            return ResponseEntity.badRequest().body("提取失败");
        }
        //将仓库中的商品删除或者修改,转移到商品临时记录,并记录到转换表中
        warehouseProductService.batchDeleteOrUpdateWarehouseProduct(pair.getSecond(), orderCode, remark, true);
        return ResponseEntity.ok(pair);
    }

    @ApiOperation("转出用户仓库申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "warehouseId", value = "仓库Id", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "query", name = "orderCode", value = "订单编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "remark", value = "备注", dataType = "String"),
            @ApiImplicitParam(paramType = "body", name = "warehouseProductParamList", dataType = "WarehouseProductParam", dataTypeClass = WarehouseProductParam.class,
                    allowMultiple = true, value = "转出申请商品列表", required = true)
    })
    @PostMapping("/warehouse/products/out-apply")
    @WarehouseProductConvert(warehouseId = "#warehouseId")
    public ResponseEntity<?> apply(@RequestParam @NotNull @Min(1) Long warehouseId,
                                   @RequestParam @NotNull String orderCode,
                                   @RequestParam String remark,
                                   @RequestBody List<WarehouseProductParam> warehouseProductParamList) {
        //校验用户仓库商品是否足够提取申请 如果足够将商品转入暂存表中等待提取

        Pair<Integer, List<WarehouseProduct>> pair = warehouseProductService.waitExtractWarehouseProduct(warehouseId, warehouseProductParamList);
        if (Objects.isNull(pair)) {
            return ResponseEntity.badRequest().body("提取失败");
        }
        //将仓库中的商品删除或者修改,转移到商品临时记录,并记录到转换表中
        warehouseProductService.batchDeleteOrUpdateWarehouseProduct(pair.getSecond(), orderCode, remark, false);
        return ResponseEntity.ok(pair);
    }

    @ApiOperation("转出用户仓库取消")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderCode", value = "订单code", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "backCause", value = "取消原因", required = true, dataType = "String")
    })
    @PutMapping("/warehouse/products/out-back")
    public ResponseEntity<?> back(@RequestParam @NotNull @Min(1) String orderCode, @RequestParam String backCause) {

        //查找中间表中是否存在要退回的仓库商品
        WarehouseProductExtract warehouseProductExtractSearch = new WarehouseProductExtract();
        warehouseProductExtractSearch.setOrderCode(orderCode);

        List<WarehouseProductExtract> warehouseProductExtractList = warehouseProductExtractService.list(warehouseProductExtractSearch);
        if (Objects.isNull(warehouseProductExtractList) || CollectionUtils.isEmpty(warehouseProductExtractList)) {
            return ResponseEntity.badRequest().body("未找到指定订单的仓库退回商品");
        }
        warehouseProductService.cancelApplyWarehouseProduct(warehouseProductExtractList, orderCode, backCause);
        return ResponseEntity.ok(orderCode);
    }

    @ApiOperation("转出用户仓库确认")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "orderCode", value = "订单Code", required = true, dataType = "String")
    })
    @PutMapping("/warehouse/products/out-confirm")
    public ResponseEntity<?> confirm(@RequestParam @NotNull String orderCode) {
        //将用户仓库商品从暂存表中删除 这样用户永久将仓库商品转移到其他用户或者提取订单
        WarehouseProductExtract warehouseProductExtractSearch = new WarehouseProductExtract();
        warehouseProductExtractSearch.setOrderCode(orderCode);

        List<WarehouseProductExtract> warehouseProductExtractList = warehouseProductExtractService.list(warehouseProductExtractSearch);
        if (Objects.isNull(warehouseProductExtractList) || CollectionUtils.isEmpty(warehouseProductExtractList)) {
            return ResponseEntity.badRequest().body("未找到指定订单的仓库确认转出商品");
        }
        //确认从仓库移除商品
        warehouseProductExtractService.confirmApplyWarehouseProduct(warehouseProductExtractList);
        return ResponseEntity.ok(orderCode);
    }
}
