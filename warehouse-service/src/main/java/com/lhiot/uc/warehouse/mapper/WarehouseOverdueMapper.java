package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.entity.WarehouseOverdue;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Description:仓库商品过期降价值处理Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
@Repository
public interface WarehouseOverdueMapper {

    /**
    * Description:新增仓库商品过期降价值处理
    *
    * @param warehouseOverdue WarehouseOverdue
    * @return int
    */
    int add(WarehouseOverdue warehouseOverdue);

    /**
    * Description:根据id修改仓库商品过期降价值处理
    *
    * @param warehouseOverdue WarehouseOverdue
    * @return int
    */
    int updateById(WarehouseOverdue warehouseOverdue);

    /**
    * Description:根据ids删除仓库商品过期降价值处理
    *
    * @param ids List<String>
    * @return int
    */
    int deleteByIds(List<String> ids);

    /**
    * Description:根据id查找仓库商品过期降价值处理
    *
    * @param id Long
    * @return WarehouseOverdue
    */
    WarehouseOverdue selectById(Long id);

    /**
    * Description:查询仓库商品过期降价值处理列表
    *
    * @param warehouseOverdue WarehouseOverdue
    * @return List<WarehouseOverdue>
    */
     List<WarehouseOverdue> pageWarehouseOverdues(WarehouseOverdue warehouseOverdue);


    /**
    * Description: 查询仓库商品过期降价值处理总记录数
    *
    * @param warehouseOverdue WarehouseOverdue
    * @return int
    */
    int pageWarehouseOverdueCounts(WarehouseOverdue warehouseOverdue);
}
