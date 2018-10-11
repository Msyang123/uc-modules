package com.lhiot.uc.warehouse.service;

import com.leon.microx.support.result.Pages;
import com.lhiot.uc.warehouse.entity.WarehouseOverdue;
import com.lhiot.uc.warehouse.mapper.WarehouseOverdueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Description:仓库商品过期降价值处理服务类
 *
 * @author yijun
 * @date 2018/09/07
 */
@Service
@Transactional
public class WarehouseOverdueService {

    private final WarehouseOverdueMapper warehouseOverdueMapper;

    @Autowired
    public WarehouseOverdueService(WarehouseOverdueMapper warehouseOverdueMapper) {
        this.warehouseOverdueMapper = warehouseOverdueMapper;
    }

    /**
     * Description:新增仓库商品过期降价值处理
     *
     * @param warehouseOverdue WarehouseOverdue
     * @return int
     */
    public int add(WarehouseOverdue warehouseOverdue) {
        return this.warehouseOverdueMapper.add(warehouseOverdue);
    }

    /**
     * Description:根据id修改仓库商品过期降价值处理
     *
     * @param warehouseOverdue WarehouseOverdue
     * @return int
     */
    public int updateById(WarehouseOverdue warehouseOverdue) {
        return this.warehouseOverdueMapper.updateById(warehouseOverdue);
    }

    /**
     * Description:根据ids删除仓库商品过期降价值处理
     *
     * @param ids String
     * @return int
     */
    public int deleteByIds(String ids) {
        return this.warehouseOverdueMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }

    /**
     * Description:根据id查找仓库商品过期降价值处理
     *
     * @param id Long
     * @return WarehouseOverdue
     */
    public WarehouseOverdue selectById(Long id) {
        return this.warehouseOverdueMapper.selectById(id);
    }

    /**
     * Description: 查询仓库商品过期降价值处理分页列表
     *
     * @param warehouseOverdue WarehouseOverdue
     * @return Pages<WarehouseOverdue>
     */
    public Pages<WarehouseOverdue> pageList(WarehouseOverdue warehouseOverdue) {
        return Pages.of(this.warehouseOverdueMapper.pageWarehouseOverdueCounts(warehouseOverdue),
                this.warehouseOverdueMapper.pageWarehouseOverdues(warehouseOverdue));
    }
}

