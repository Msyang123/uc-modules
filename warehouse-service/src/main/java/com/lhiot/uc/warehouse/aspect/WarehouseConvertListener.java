package com.lhiot.uc.warehouse.aspect;

import com.leon.microx.util.Beans;
import com.leon.microx.util.Calculator;
import com.leon.microx.util.Maps;
import com.lhiot.uc.warehouse.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.entity.WarehouseOverdue;
import com.lhiot.uc.warehouse.entity.WarehouseProduct;
import com.lhiot.uc.warehouse.entity.WarehouseUser;
import com.lhiot.uc.warehouse.feign.BasicUserService;
import com.lhiot.uc.warehouse.mapper.WarehouseConvertMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseOverdueMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseProductMapper;
import com.lhiot.uc.warehouse.mapper.WarehouseUserMapper;
import com.lhiot.uc.warehouse.model.ConvertType;
import com.lhiot.uc.warehouse.model.InOutType;
import com.lhiot.uc.warehouse.model.WarehouseConvertTaskParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangfeng create in 8:57 2018/10/16
 */
@Slf4j
@Component
public class WarehouseConvertListener {

    private final WarehouseProductMapper warehouseProductMapper;
    private final WarehouseConvertMapper warehouseConvertMapper;
    private final WarehouseOverdueMapper warehouseOverdueMapper;
    private final WarehouseUserMapper warehouseUserMapper;
    private final BasicUserService basicUserService;

    public WarehouseConvertListener(WarehouseProductMapper warehouseProductMapper, WarehouseConvertMapper warehouseConvertMapper, WarehouseOverdueMapper warehouseOverdueMapper, WarehouseUserMapper warehouseUserMapper, BasicUserService basicUserService) {
        this.warehouseProductMapper = warehouseProductMapper;
        this.warehouseConvertMapper = warehouseConvertMapper;
        this.warehouseOverdueMapper = warehouseOverdueMapper;
        this.warehouseUserMapper = warehouseUserMapper;
        this.basicUserService = basicUserService;
    }

    @EventListener
    public void onConverting(WarehouseConvertTaskParam event) {
        List<OverdueHandler> handlers = new ArrayList<>(2);
        handlers.add(this::onApplicationOverdueProduct);
        handlers.add(this::onApplicationConvertFruitCoin);
        handlers.parallelStream().forEach(handler -> handler.handle(event));
    }

    interface OverdueHandler {
        void handle(WarehouseConvertTaskParam param);
    }

    private void onApplicationOverdueProduct(WarehouseConvertTaskParam taskParam) {
        log.info("仓库转过期水果");
        List<WarehouseProduct> productList = warehouseProductMapper.findOverdueProduct(Maps.of("freshDay", taskParam.getFreshDay(), "warehouseId", taskParam.getWarehouseId()));
        if (!CollectionUtils.isEmpty(productList)) {
            List<Long> warehouseProductIds = new ArrayList<>();
            List<WarehouseOverdue> overdueList = new ArrayList<>();
            productList.forEach(product -> {
                WarehouseOverdue overdue = Beans.from(product).populate(WarehouseOverdue::new);
                overdueList.add(overdue);
                warehouseProductIds.add(product.getId());
            });
            warehouseOverdueMapper.batchInsert(overdueList);
            warehouseProductMapper.deleteByIds(warehouseProductIds);

            //TODO 消息推送的实现
        }
    }

    private void onApplicationConvertFruitCoin(WarehouseConvertTaskParam taskParam) {
        log.info("仓库转鲜果币");
        List<WarehouseOverdue> systemConvertList = warehouseOverdueMapper.findConvertProduct(Maps.of("day", taskParam.getSystemConvertDay(), "warehouseId", taskParam.getWarehouseId()));
        if (!CollectionUtils.isEmpty(systemConvertList)) {
            List<WarehouseConvert> convertList = new ArrayList<>();
            List<String> overdueIds = new ArrayList<>();
            int[] totalPrice = {0};
            systemConvertList.forEach(convertProduct -> {
                totalPrice[0] += convertProduct.getPrice();
                WarehouseConvert convert = Beans.from(convertProduct).excludes("id").populate(WarehouseConvert::new);
                convert.setConvertType(ConvertType.AUTO);
                convert.setDiscount(taskParam.getSystemDiscount());
                convert.setInOut(InOutType.OUT);
                convert.setRemark("仓库转换鲜果币");
                convert.setConvertAt(Date.from(Instant.now()));
                convertList.add(convert);
                overdueIds.add(String.valueOf(convertProduct.getId()));
            });
            //添加用户鲜果币
            Double money = Calculator.div(Calculator.mul(totalPrice[0], taskParam.getSystemDiscount()), 100.0, 0);
            WarehouseUser warehouseUser = warehouseUserMapper.selectById(Long.valueOf(taskParam.getWarehouseId()));
            ResponseEntity response = basicUserService.addBalance(warehouseUser.getBaseUserId(), money.intValue());
            if (response.getStatusCode().isError()) {
                return;
            }
            //删除过期表中的记录
            warehouseOverdueMapper.deleteByIds(overdueIds);
            //添加转换记录表记录
            warehouseConvertMapper.saveWarehouseConvertBatch(convertList);
            //TODO 进行消息推送
        }
    }
}
