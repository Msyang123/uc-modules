package com.lhiot.uc.warehouse.service;

import com.leon.microx.support.result.Pages;
import com.lhiot.uc.warehouse.entity.WarehouseProductExtract;
import com.lhiot.uc.warehouse.mapper.WarehouseProductExtractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:仓库商品提取服务类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Service
@Transactional
public class WarehouseProductExtractService {

    private final WarehouseProductExtractMapper warehouseProductExtractMapper;

    @Autowired
    public WarehouseProductExtractService(WarehouseProductExtractMapper warehouseProductExtractMapper) {
        this.warehouseProductExtractMapper = warehouseProductExtractMapper;
    }

    /**
     * Description:新增仓库商品提取
     *
     * @param warehouseProductExtract
     * @return
     * @author yijun
     * @date 2018/09/07 11:36:51
     */
/*    public int batchSaveExtract(List<WarehouseProductExtract> warehouseProductExtract){
        return this.warehouseProductExtractMapper.batchSaveExtract(warehouseProductExtract);
    }*/

    /**
     * Description:根据id修改仓库商品提取
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return int
     */
    public int updateById(WarehouseProductExtract warehouseProductExtract) {
        return this.warehouseProductExtractMapper.updateById(warehouseProductExtract);
    }

    /**
     * Description:根据ids删除仓库商品提取
     *
     * @param ids
     * @return
     * @author yijun
     * @date 2018/09/07 11:36:51
     */
/*    public int deleteByIds(String ids){
        return this.warehouseProductExtractMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }*/

    /**
     * Description:根据id查找仓库商品提取
     *
     * @param id Long
     * @return WarehouseProductExtract
     */
    public WarehouseProductExtract selectById(Long id) {
        return this.warehouseProductExtractMapper.selectById(id);
    }


    /**
     * Description: 查询仓库商品提取分页列表
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return Pages<WarehouseProductExtract>
     */
    public Pages<WarehouseProductExtract> pageList(WarehouseProductExtract warehouseProductExtract) {
        return Pages.of(this.warehouseProductExtractMapper.pageWarehouseProductExtractCounts(warehouseProductExtract),
                list(warehouseProductExtract));
    }

    /**
     * Description: 查询仓库商品提取
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return List<WarehouseProductExtract>
     */
    public List<WarehouseProductExtract> list(WarehouseProductExtract warehouseProductExtract){
        return this.warehouseProductExtractMapper.pageWarehouseProductExtracts(warehouseProductExtract);
    }


    /**
     * 从仓库商品中间表中永久扣除仓库确认商品
     */
    public void confirmApplyWarehouseProduct(List<WarehouseProductExtract> cancelWarehouseProductExtractList) {
        final List<Long> productExtractIds = new ArrayList<>(cancelWarehouseProductExtractList.size());
        cancelWarehouseProductExtractList.forEach(item -> productExtractIds.add(item.getId()));
        //移除中间表数据
        warehouseProductExtractMapper.deleteByIds(productExtractIds);
    }
}

