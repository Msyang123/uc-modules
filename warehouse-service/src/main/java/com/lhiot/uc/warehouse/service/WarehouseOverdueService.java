package com.lhiot.uc.warehouse.service;

import com.lhiot.uc.warehouse.domain.entity.WarehouseOverdue;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.mapper.WarehouseOverdueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
* Description:仓库商品过期降价值处理服务类
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
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public int create(WarehouseOverdue warehouseOverdue){
        return this.warehouseOverdueMapper.create(warehouseOverdue);
    }

    /** 
    * Description:根据id修改仓库商品过期降价值处理
    *  
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public int updateById(WarehouseOverdue warehouseOverdue){
        return this.warehouseOverdueMapper.updateById(warehouseOverdue);
    }

    /** 
    * Description:根据ids删除仓库商品过期降价值处理
    *  
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public int deleteByIds(String ids){
        return this.warehouseOverdueMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }
    
    /** 
    * Description:根据id查找仓库商品过期降价值处理
    *  
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public WarehouseOverdue selectById(Long id){
        return this.warehouseOverdueMapper.selectById(id);
    }

    /** 
    * Description: 查询仓库商品过期降价值处理总记录数
    *  
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public long count(WarehouseOverdue warehouseOverdue){
        return this.warehouseOverdueMapper.pageWarehouseOverdueCounts(warehouseOverdue);
    }
    
    /** 
    * Description: 查询仓库商品过期降价值处理分页列表
    *  
    * @param warehouseOverdue
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public PagerResultObject<WarehouseOverdue> pageList(WarehouseOverdue warehouseOverdue) {
       long total = 0;
       if (warehouseOverdue.getRows() != null && warehouseOverdue.getRows() > 0) {
           total = this.count(warehouseOverdue);
       }
       return PagerResultObject.of(warehouseOverdue, total,
              this.warehouseOverdueMapper.pageWarehouseOverdues(warehouseOverdue));
    }
}

