package com.lhiot.uc.warehouse.api;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Pair;
import com.lhiot.uc.warehouse.domain.entity.WarehouseGoods;
import com.lhiot.uc.warehouse.domain.entity.WarehouseGoodsExtract;
import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.domain.model.WarehouseGoodsParam;
import com.lhiot.uc.warehouse.service.WarehouseGoodsExtractService;
import com.lhiot.uc.warehouse.service.WarehouseGoodsService;
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
* @author yijun
* @date 2018/09/07
*/
@Api(description = "仓库商品接口")
@Slf4j
@RestController
@RequestMapping("/warehouse-goods")
public class WarehouseGoodsApi {

    private final WarehouseUserService warehouseUserService;
    private final WarehouseGoodsService warehouseGoodsService;
    private final WarehouseGoodsExtractService warehouseGoodsExtractService;

    @Autowired
    public WarehouseGoodsApi(WarehouseUserService warehouseUserService, WarehouseGoodsService warehouseGoodsService, WarehouseGoodsExtractService warehouseGoodsExtractService) {
        this.warehouseUserService = warehouseUserService;
        this.warehouseGoodsService = warehouseGoodsService;
        this.warehouseGoodsExtractService = warehouseGoodsExtractService;
    }

    
    @ApiOperation(value = "根据id查询仓库商品", notes = "根据id查询仓库商品")
    @ApiImplicitParam(paramType = "path", name = "id", value = "主键id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseGoods> findWarehouseGoods(@PathVariable("id") Long id) {

        return ResponseEntity.ok(warehouseGoodsService.selectById(id));
    }
    
    @RequestMapping(value = "/page/select", method = RequestMethod.POST)
    @ApiOperation(value = "查询仓库商品分页列表")
    public ResponseEntity<PagerResultObject<WarehouseGoods>> pageSelect(@RequestBody WarehouseGoods warehouseGoods){
        log.debug("查询仓库商品分页列表\t param:{}",warehouseGoods);
        if(Objects.isNull(warehouseGoods)){
            ResponseEntity.badRequest().body("传递参数错误");
        }
        WarehouseUser warehouseUser = warehouseUserService.selectById(warehouseGoods.getWarehouseId());

        if(Objects.isNull(warehouseUser)){
            ResponseEntity.badRequest().body("未找到仓库错误");
        }
        return ResponseEntity.ok(warehouseGoodsService.pageList(warehouseGoods));
    }

    @ApiOperation(value = "水果仓库商品分类查询", notes = "水果仓库商品分类查询")
    @ApiImplicitParam(paramType = "param", name = "baseUserId", value = "基础用户id", required = true, dataType = "long")
    @RequestMapping(value = "/select/{baseUserId}", method = RequestMethod.GET)
    public ResponseEntity getWhGoods(@RequestParam("baseUserId") Long baseUserId) {

        if(Objects.isNull(baseUserId)){
            ResponseEntity.badRequest().body("传递参数错误");
        }
        WarehouseUser warehouseUser = warehouseUserService.findByBaseUserId(baseUserId);
        if(Objects.isNull(warehouseUser)){
            return ResponseEntity.badRequest().body("未找到仓库");
        }

        WarehouseGoods warehouseGoods=new WarehouseGoods();
        warehouseGoods.setWarehouseId(warehouseUser.getId());

        return ResponseEntity.ok(warehouseGoodsService.pageList(warehouseGoods));
    }


    /*************************用户仓库存储与提取******************************************************/
    @ApiOperation("存入用户仓库")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="baseUserId", value="基础用户Id",required=true,dataType="Long"),
            @ApiImplicitParam(paramType = "body", name ="warehouseGoodsParamList",dataType="WarehouseGoodsParam",dataTypeClass = WarehouseGoodsParam.class,
                    allowMultiple = true,value="存入仓库商品列表",required=true),
            @ApiImplicitParam(paramType="query",name="remark", value="备注",dataType="String")
    })
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @RequestParam @NotNull @Min(1) Long baseUserId,
            @RequestBody List<WarehouseGoodsParam> warehouseGoodsParamList,
            @RequestParam String remark){
        if (Objects.isNull(warehouseGoodsParamList)||warehouseGoodsParamList.isEmpty()){
            return ResponseEntity.badRequest().body("存入仓库商品列表为空");
        }
        //查找用户仓库
        WarehouseUser warehouseUser = warehouseUserService.findByBaseUserId(baseUserId);
        if(Objects.isNull(warehouseUser)){
            return ResponseEntity.badRequest().body("未找到用户仓库");
        }
        //TODO 需要查询基础商品信息构造仓库商品信息
        List<WarehouseGoods> warehouseGoodsList=new ArrayList<>(warehouseGoodsParamList.size());
        warehouseGoodsParamList.forEach(item->{
            WarehouseGoods warehouseGoods=new WarehouseGoods();
            warehouseGoods.setWarehouseId(warehouseUser.getId());
            BeanUtils.of(warehouseGoods).populate(item);
        });

        boolean result=warehouseGoodsService.addWarehouseGoods(warehouseGoodsList,baseUserId, remark);
        if(result){
            return ResponseEntity.ok("存入成功");
        }else{
            return ResponseEntity.badRequest().body("存入用户仓库失败");
        }
    }
    @ApiOperation("转出用户仓库申请")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="baseUserId", value="基础用户Id",required=true,dataType="Long"),
            @ApiImplicitParam(paramType="query",name="orderCode", value="订单编码",required=true,dataType="String"),
            @ApiImplicitParam(paramType="query",name="remark", value="备注",dataType="String"),
            @ApiImplicitParam(paramType = "body", name ="warehouseGoodsParamList",dataType="WarehouseGoodsParam",dataTypeClass = WarehouseGoodsParam.class,
                    allowMultiple = true,value="转出申请商品列表",required=true)
    })
    @PostMapping("/out/apply")
    public ResponseEntity<?> apply(@RequestParam @NotNull @Min(1) Long baseUserId,
                                   @RequestParam @NotNull String orderCode,
            @RequestParam String remark,
            @RequestBody List<WarehouseGoodsParam> warehouseGoodsParamList){
        //校验用户仓库商品是否足够提取申请 如果足够将商品转入暂存表中等待提取

        Pair<Integer, List<WarehouseGoods>> pair =warehouseGoodsService.waitExtractWarehouseGoods(baseUserId,warehouseGoodsParamList);
        if (Objects.isNull(pair)) {
            return ResponseEntity.badRequest().body("提取失败");
        }
        //将仓库中的商品删除或者修改,转移到商品临时记录,并记录到转换表中
        warehouseGoodsService.batchDeleteOrUpdateWarehouseGoods(pair.getSecond(),orderCode,remark);
        return ResponseEntity.ok(pair);
    }
    @ApiOperation("转出用户仓库取消")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="orderCode", value="订单code",required=true,dataType="String"),
            @ApiImplicitParam(paramType="query",name="backCause", value="取消原因",required=true,dataType="String")
    })
    @PutMapping("/out/cancel")
    public ResponseEntity<?> cancel(@RequestParam @NotNull @Min(1) String orderCode,@RequestParam String backCause){

        //查找中间表中是否存在要退回的仓库商品
        WarehouseGoodsExtract warehouseGoodsExtractSearch=new WarehouseGoodsExtract();
        warehouseGoodsExtractSearch.setOrderCode(orderCode);

        PagerResultObject<WarehouseGoodsExtract> pagerResultObject = warehouseGoodsExtractService.pageList(warehouseGoodsExtractSearch);
        if(Objects.isNull(pagerResultObject)|| CollectionUtils.isEmpty(pagerResultObject.getArray())){
            return ResponseEntity.badRequest().body("未找到指定订单的仓库退回商品");
        }
        warehouseGoodsService.caneclAppayWarehouseGoods(pagerResultObject.getArray(),orderCode,backCause);
        return ResponseEntity.ok(orderCode);
    }
    @ApiOperation("转出用户仓库确认")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query",name="orderCode", value="订单Code",required=true,dataType="String")
    })
    @PutMapping("/out/confirm")
    public ResponseEntity<?> confirm(@RequestParam @NotNull String orderCode){
        //将用户仓库商品从暂存表中删除 这样用户永久将仓库商品转移到其他用户或者提取订单
        WarehouseGoodsExtract warehouseGoodsExtractSearch=new WarehouseGoodsExtract();
        warehouseGoodsExtractSearch.setOrderCode(orderCode);

        PagerResultObject<WarehouseGoodsExtract> pagerResultObject = warehouseGoodsExtractService.pageList(warehouseGoodsExtractSearch);
        if(Objects.isNull(pagerResultObject)|| CollectionUtils.isEmpty(pagerResultObject.getArray())){
            return ResponseEntity.badRequest().body("未找到指定订单的仓库确认转出商品");
        }
        //确认从仓库移除商品
        warehouseGoodsExtractService.confirmAppayWarehouseGoods(pagerResultObject.getArray());
        return ResponseEntity.ok(orderCode);
    }
}
