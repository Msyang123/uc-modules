package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.entity.WarehouseProductExtract;
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
public interface WarehouseProductExtractMapper {

    /**
     * Description:批量新增仓库商品提取
     *
     * @param warehouseProductExtractList List<WarehouseProductExtract>
     * @return int
     */
    int batchSaveExtract(List<WarehouseProductExtract> warehouseProductExtractList);

    /**
     * Description:根据id修改仓库商品提取
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return int
     */
    int updateById(WarehouseProductExtract warehouseProductExtract);

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
     * @return WarehouseProductExtract
     */
    WarehouseProductExtract selectById(Long id);

    /**
     * Description:查询仓库商品提取列表
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return List<WarehouseProductExtract>
     */
    List<WarehouseProductExtract> pageWarehouseProductExtracts(WarehouseProductExtract warehouseProductExtract);


    /**
     * Description: 查询仓库商品提取总记录数
     *
     * @param warehouseProductExtract WarehouseProductExtract
     * @return int
     */
    int pageWarehouseProductExtractCounts(WarehouseProductExtract warehouseProductExtract);
}
