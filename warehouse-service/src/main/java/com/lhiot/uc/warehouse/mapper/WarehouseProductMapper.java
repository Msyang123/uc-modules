package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.entity.WarehouseProduct;
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
public interface WarehouseProductMapper {

    /**
     * Description:批量新增仓库商品
     *
     * @param warehouseProductList List<WarehouseProduct>
     * @return int
     */
    int batchSave(List<WarehouseProduct> warehouseProductList);


    int updateCountAndPrice(WarehouseProduct warehouseProduct);

    /**
     * Description:根据id修改仓库商品
     *
     * @param warehouseProduct WarehouseProduct
     * @return int
     */
    int updateById(WarehouseProduct warehouseProduct);

    /**
     * Description:根据id修改仓库商品数量
     *
     * @param warehouseProduct WarehouseProduct
     * @return int
     */
    int updateProductCount(WarehouseProduct warehouseProduct);


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
     * @return WarehouseProduct
     */
    WarehouseProduct selectById(Long id);


    /**
     * Description:查询仓库商品列表
     *
     * @param warehouseProduct WarehouseProduct
     * @return List<WarehouseProduct>
     */
    List<WarehouseProduct> pageWarehouseProducts(WarehouseProduct warehouseProduct);


    /**
     * Description: 查询仓库商品总记录数
     *
     * @param warehouseProduct WarehouseProduct
     * @return int
     */
    int pageWarehouseProductCounts(WarehouseProduct warehouseProduct);

    /**
     * 查询当天的水果清单
     *
     * @param warehouseId Long
     * @return List<WarehouseProduct>
     */
    List<WarehouseProduct> findWarehouseProductByWareHouseIdAndToday(Long warehouseId);
}
