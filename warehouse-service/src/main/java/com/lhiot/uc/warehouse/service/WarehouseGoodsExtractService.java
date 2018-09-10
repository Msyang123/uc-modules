package com.lhiot.uc.warehouse.service;

import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.domain.entity.WarehouseGoodsExtract;
import com.lhiot.uc.warehouse.mapper.WarehouseGoodsExtractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* Description:仓库商品提取服务类
* @author yijun
* @date 2018/09/07
*/
@Service
@Transactional
public class WarehouseGoodsExtractService {

    private final WarehouseGoodsExtractMapper warehouseGoodsExtractMapper;

    @Autowired
    public WarehouseGoodsExtractService(WarehouseGoodsExtractMapper warehouseGoodsExtractMapper) {
        this.warehouseGoodsExtractMapper = warehouseGoodsExtractMapper;
    }

    /** 
    * Description:新增仓库商品提取
    *  
    * @param warehouseGoodsExtract
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
/*    public int batchSaveExtract(List<WarehouseGoodsExtract> warehouseGoodsExtract){
        return this.warehouseGoodsExtractMapper.batchSaveExtract(warehouseGoodsExtract);
    }*/

    /** 
    * Description:根据id修改仓库商品提取
    *  
    * @param warehouseGoodsExtract
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public int updateById(WarehouseGoodsExtract warehouseGoodsExtract){
        return this.warehouseGoodsExtractMapper.updateById(warehouseGoodsExtract);
    }

    /** 
    * Description:根据ids删除仓库商品提取
    *  
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
/*    public int deleteByIds(String ids){
        return this.warehouseGoodsExtractMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }*/
    
    /** 
    * Description:根据id查找仓库商品提取
    *  
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public WarehouseGoodsExtract selectById(Long id){
        return this.warehouseGoodsExtractMapper.selectById(id);
    }

    /** 
    * Description: 查询仓库商品提取总记录数
    *  
    * @param warehouseGoodsExtract
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public long count(WarehouseGoodsExtract warehouseGoodsExtract){
        return this.warehouseGoodsExtractMapper.pageWarehouseGoodsExtractCounts(warehouseGoodsExtract);
    }
    
    /** 
    * Description: 查询仓库商品提取分页列表
    *  
    * @param warehouseGoodsExtract
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public PagerResultObject<WarehouseGoodsExtract> pageList(WarehouseGoodsExtract warehouseGoodsExtract) {
       long total = 0;
       if (warehouseGoodsExtract.getRows() != null && warehouseGoodsExtract.getRows() > 0) {
           total = this.count(warehouseGoodsExtract);
       }
       return PagerResultObject.of(warehouseGoodsExtract, total,
              this.warehouseGoodsExtractMapper.pageWarehouseGoodsExtracts(warehouseGoodsExtract));
    }


    /**
     * 从仓库商品中间表中永久扣除仓库确认商品
     */
    public void confirmAppayWarehouseGoods(List<WarehouseGoodsExtract> cancelWarehouseGoodsExtractList){
        final List<Long> goodsExtractIds=new ArrayList<>(cancelWarehouseGoodsExtractList.size());
        cancelWarehouseGoodsExtractList.forEach(item->goodsExtractIds.add(item.getId()));
        //移除中间表数据
        warehouseGoodsExtractMapper.deleteByIds(goodsExtractIds);
    }
}

