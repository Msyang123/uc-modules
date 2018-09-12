package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseOverdue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* Description:仓库商品过期降价值处理Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
public interface WarehouseOverdueMapper {

    /**
    * Description:新增仓库商品过期降价值处理
    *
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int add(WarehouseOverdue warehouseOverdue);

    /**
    * Description:根据id修改仓库商品过期降价值处理
    *
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int updateById(WarehouseOverdue warehouseOverdue);

    /**
    * Description:根据ids删除仓库商品过期降价值处理
    *
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int deleteByIds(List<String> ids);

    /**
    * Description:根据id查找仓库商品过期降价值处理
    *
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    WarehouseOverdue selectById(Long id);

    /**
    * Description:查询仓库商品过期降价值处理列表
    *
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
     List<WarehouseOverdue> pageWarehouseOverdues(WarehouseOverdue warehouseOverdue);


    /**
    * Description: 查询仓库商品过期降价值处理总记录数
    *
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    long pageWarehouseOverdueCounts(WarehouseOverdue warehouseOverdue);
}
