package com.lhiot.uc.warehouse.service;

import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import com.lhiot.uc.warehouse.domain.common.PagerResultObject;
import com.lhiot.uc.warehouse.mapper.WarehouseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
* Description:用户仓库服务类
* @author yijun
* @date 2018/09/07
*/
@Service
@Transactional
public class WarehouseUserService {

    private final WarehouseUserMapper warehouseUserMapper;

    @Autowired
    public WarehouseUserService(WarehouseUserMapper warehouseUserMapper) {
        this.warehouseUserMapper = warehouseUserMapper;
    }

    /** 
    * Description:新增用户仓库
    *  
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public int create(WarehouseUser warehouseUser){
        return this.warehouseUserMapper.create(warehouseUser);
    }

    /** 
    * Description:根据id修改用户仓库
    *  
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public int updateById(WarehouseUser warehouseUser){
        return this.warehouseUserMapper.updateById(warehouseUser);
    }

    /** 
    * Description:根据ids删除用户仓库
    *  
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public int deleteByIds(String ids){
        return this.warehouseUserMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }
    
    /** 
    * Description:根据id查找用户仓库
    *  
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */ 
    public WarehouseUser selectById(Long id){
        return this.warehouseUserMapper.selectById(id);
    }

    /**
     * Description:根据id查找用户仓库
     *
     * @param baseUserId
     * @return
     * @author yijun
     * @date 2018/09/07 11:36:51
     */
    public WarehouseUser findByBaseUserId(Long baseUserId){
        return this.warehouseUserMapper.findByBaseUserId(baseUserId);
    }

    /** 
    * Description: 查询用户仓库总记录数
    *  
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public long count(WarehouseUser warehouseUser){
        return this.warehouseUserMapper.pageWarehouseUserCounts(warehouseUser);
    }
    
    /** 
    * Description: 查询用户仓库分页列表
    *  
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */  
    public PagerResultObject<WarehouseUser> pageList(WarehouseUser warehouseUser) {
       long total = 0;
       if (warehouseUser.getRows() != null && warehouseUser.getRows() > 0) {
           total = this.count(warehouseUser);
       }
       return PagerResultObject.of(warehouseUser, total,
              this.warehouseUserMapper.pageWarehouseUsers(warehouseUser));
    }
}
