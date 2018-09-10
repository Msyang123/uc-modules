package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseConvert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* Description:仓库出入库记录明细Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
public interface WarehouseConvertMapper {

    /**
    * Description:批量新增仓库出入库记录明细
    *
    * @param warehouseConvertList
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int saveWarehouseConvertBatch(List<WarehouseConvert> warehouseConvertList);

    /**
    * Description:根据id修改仓库出入库记录明细
    *
    * @param warehouseConvert
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int updateById(WarehouseConvert warehouseConvert);

    /**
    * Description:根据ids删除仓库出入库记录明细
    *
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int deleteByIds(List<String> ids);

    /**
    * Description:根据id查找仓库出入库记录明细
    *
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    WarehouseConvert selectById(Long id);

    /**
    * Description:查询仓库出入库记录明细列表
    *
    * @param warehouseConvert
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
     List<WarehouseConvert> pageWarehouseConverts(WarehouseConvert warehouseConvert);


    /**
    * Description: 查询仓库出入库记录明细总记录数
    *
    * @param warehouseConvert
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    long pageWarehouseConvertCounts(WarehouseConvert warehouseConvert);
}
