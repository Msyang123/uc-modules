package com.lhiot.uc.warehouse.service;

import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.domain.entity.WarehouseConvert;
import com.lhiot.uc.warehouse.mapper.WarehouseConvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Description:仓库出入库记录明细服务类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Service
@Transactional
public class WarehouseConvertService {

    private final WarehouseConvertMapper warehouseConvertMapper;

    @Autowired
    public WarehouseConvertService(WarehouseConvertMapper warehouseConvertMapper) {
        this.warehouseConvertMapper = warehouseConvertMapper;
    }

    /**
     * Description:新增仓库出入库记录明细
     *
     * @param warehouseConvertList List<WarehouseConvert>
     * @return int
     */
    public int saveWarehouseConvertBatch(List<WarehouseConvert> warehouseConvertList) {
        return this.warehouseConvertMapper.saveWarehouseConvertBatch(warehouseConvertList);
    }

    /**
     * Description:根据id修改仓库出入库记录明细
     *
     * @param warehouseConvert WarehouseConvert
     * @return int
     */
    public int updateById(WarehouseConvert warehouseConvert) {
        return this.warehouseConvertMapper.updateById(warehouseConvert);
    }

    /**
     * Description:根据ids删除仓库出入库记录明细
     *
     * @param ids String
     * @return int
     */
    public int deleteByIds(String ids) {
        return this.warehouseConvertMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }

    /**
     * Description:根据id查找仓库出入库记录明细
     *
     * @param id Long
     * @return WarehouseConvert
     */
    public WarehouseConvert selectById(Long id) {
        return this.warehouseConvertMapper.selectById(id);
    }

    /**
     * Description: 查询仓库出入库记录明细总记录数
     *
     * @param warehouseConvert WarehouseConvert
     * @return long
     */
    public long count(WarehouseConvert warehouseConvert) {
        return this.warehouseConvertMapper.pageWarehouseConvertCounts(warehouseConvert);
    }

    /**
     * Description: 查询仓库出入库记录明细分页列表
     *
     * @param warehouseConvert WarehouseConvert
     * @return PagerResultObject<WarehouseConvert>
     */
    public PagerResultObject<WarehouseConvert> pageList(WarehouseConvert warehouseConvert) {
        long total = 0;
        if (warehouseConvert.getRows() != null && warehouseConvert.getRows() > 0) {
            total = this.count(warehouseConvert);
        }
        return PagerResultObject.of(warehouseConvert, total,
                this.warehouseConvertMapper.pageWarehouseConverts(warehouseConvert));
    }
}

