package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseGoodsExtract;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:仓库商品提取Mapper类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Mapper
@Repository
public interface WarehouseGoodsExtractMapper {

    /**
     * Description:批量新增仓库商品提取
     *
     * @param warehouseGoodsExtractList List<WarehouseGoodsExtract>
     * @return int
     */
    int batchSaveExtract(List<WarehouseGoodsExtract> warehouseGoodsExtractList);

    /**
     * Description:根据id修改仓库商品提取
     *
     * @param warehouseGoodsExtract WarehouseGoodsExtract
     * @return int
     */
    int updateById(WarehouseGoodsExtract warehouseGoodsExtract);

    /**
     * Description:根据ids删除仓库商品提取
     *
     * @param ids List<Long>
     * @return int
     */
    int deleteByIds(List<Long> ids);

    /**
     * Description:根据id查找仓库商品提取
     *
     * @param id Long
     * @return WarehouseGoodsExtract
     */
    WarehouseGoodsExtract selectById(Long id);

    /**
     * Description:查询仓库商品提取列表
     *
     * @param warehouseGoodsExtract WarehouseGoodsExtract
     * @return List<WarehouseGoodsExtract>
     */
    List<WarehouseGoodsExtract> pageWarehouseGoodsExtracts(WarehouseGoodsExtract warehouseGoodsExtract);


    /**
     * Description: 查询仓库商品提取总记录数
     *
     * @param warehouseGoodsExtract WarehouseGoodsExtract
     * @return long
     */
    long pageWarehouseGoodsExtractCounts(WarehouseGoodsExtract warehouseGoodsExtract);
}
