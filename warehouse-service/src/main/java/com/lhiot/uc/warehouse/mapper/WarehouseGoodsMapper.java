package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseGoods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:仓库商品Mapper类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Mapper
@Repository
public interface WarehouseGoodsMapper {

    /**
     * Description:批量新增仓库商品
     *
     * @param warehouseGoodsList List<WarehouseGoods>
     * @return int
     */
    int batchSave(List<WarehouseGoods> warehouseGoodsList);


    int updateCountAndPrice(WarehouseGoods warehouseGoods);

    /**
     * Description:根据id修改仓库商品
     *
     * @param warehouseGoods WarehouseGoods
     * @return int
     */
    int updateById(WarehouseGoods warehouseGoods);

    /**
     * Description:根据id修改仓库商品数量
     *
     * @param warehouseGoods WarehouseGoods
     * @return int
     */
    int updateGoodsCount(WarehouseGoods warehouseGoods);


    /**
     * Description:根据ids删除仓库商品
     *
     * @param ids List<Long>
     * @return int
     */
    int deleteByIds(List<Long> ids);

    /**
     * Description:根据id查找仓库商品
     *
     * @param id Long
     * @return WarehouseGoods
     */
    WarehouseGoods selectById(Long id);


    /**
     * Description:查询仓库商品列表
     *
     * @param warehouseGoods WarehouseGoods
     * @return List<WarehouseGoods>
     */
    List<WarehouseGoods> pageWarehouseGoodss(WarehouseGoods warehouseGoods);


    /**
     * Description: 查询仓库商品总记录数
     *
     * @param warehouseGoods WarehouseGoods
     * @return long
     */
    long pageWarehouseGoodsCounts(WarehouseGoods warehouseGoods);

    /**
     * 查询当天的水果清单
     *
     * @param warehouseId Long
     * @return List<WarehouseGoods>
     */
    List<WarehouseGoods> findWarehouseGoodsByWareHouseIdAndToday(Long warehouseId);
}
