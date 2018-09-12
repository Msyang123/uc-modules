package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* Description:仓库商品Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
public interface WarehouseGoodsMapper {

    /**
    * Description:批量新增仓库商品
    *
    * @param warehouseGoodsList
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int batchSave(List<WarehouseGoods> warehouseGoodsList);


    int updateCountAndPrice(WarehouseGoods warehouseGoods);

    /**
    * Description:根据id修改仓库商品
    *
    * @param warehouseGoods
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int updateById(WarehouseGoods warehouseGoods);

    /**
     * Description:根据id修改仓库商品数量
     *
     * @param warehouseGoods
     * @return
     * @author yijun
     * @date 2018/09/07 11:36:51
     */
    int updateGoodsCount(WarehouseGoods warehouseGoods);


    /**
    * Description:根据ids删除仓库商品
    *
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int deleteByIds(List<Long> ids);

    /**
    * Description:根据id查找仓库商品
    *
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    WarehouseGoods selectById(Long id);


    /**
    * Description:查询仓库商品列表
    *
    * @param warehouseGoods
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
     List<WarehouseGoods> pageWarehouseGoodss(WarehouseGoods warehouseGoods);


    /**
    * Description: 查询仓库商品总记录数
    *
    * @param warehouseGoods
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    long pageWarehouseGoodsCounts(WarehouseGoods warehouseGoods);

    /**
     * 查询当天的水果清单
     * @param warehouseId
     * @return
     */
    List<WarehouseGoods> findWarehouseGoodsByWareHouseIdAndToday(Long warehouseId);
}
