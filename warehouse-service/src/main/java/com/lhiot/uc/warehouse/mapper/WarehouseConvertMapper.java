package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.entity.WarehouseConvert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:仓库出入库记录明细Mapper类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Mapper
@Repository
public interface WarehouseConvertMapper {

    /**
     * Description:批量新增仓库出入库记录明细
     *
     * @param warehouseConvertList List<WarehouseConvert>
     * @return int
     */
    int saveWarehouseConvertBatch(List<WarehouseConvert> warehouseConvertList);

    /**
     * Description:根据id修改仓库出入库记录明细
     *
     * @param warehouseConvert WarehouseConvert
     * @return int
     */
    int updateById(WarehouseConvert warehouseConvert);

    /**
     * Description:根据ids删除仓库出入库记录明细
     *
     * @param ids List<String>
     * @return int
     */
    int deleteByIds(List<String> ids);

    /**
     * Description:根据id查找仓库出入库记录明细
     *
     * @param id Long
     * @return WarehouseConvert
     */
    WarehouseConvert selectById(Long id);

    /**
     * Description:查询仓库出入库记录明细列表
     *
     * @param warehouseConvert WarehouseConvert
     * @return  List<WarehouseConvert>
     */
    List<WarehouseConvert> pageWarehouseConverts(WarehouseConvert warehouseConvert);


    /**
     * Description: 查询仓库出入库记录明细总记录数
     *
     * @param warehouseConvert WarehouseConvert
     * @return int
     */
    int pageWarehouseConvertCounts(WarehouseConvert warehouseConvert);
}
