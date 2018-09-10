package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* Description:用户仓库Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
public interface WarehouseUserMapper {

    /**
    * Description:新增用户仓库
    *
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int add(WarehouseUser warehouseUser);

    /**
    * Description:根据id修改用户仓库
    *
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int updateById(WarehouseUser warehouseUser);

    /**
    * Description:根据ids删除用户仓库
    *
    * @param ids
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    int deleteByIds(List<String> ids);

    /**
    * Description:根据id查找用户仓库
    *
    * @param id
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    WarehouseUser selectById(Long id);


    /**
     * Description:根据基础用户id查找用户仓库
     *
     * @param baseUserId
     * @return
     * @author yijun
     * @date 2018/09/07 11:36:51
     */
    WarehouseUser findByBaseUserId(Long baseUserId);


    /**
    * Description:查询用户仓库列表
    *
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
     List<WarehouseUser> pageWarehouseUsers(WarehouseUser warehouseUser);


    /**
    * Description: 查询用户仓库总记录数
    *
    * @param warehouseUser
    * @return
    * @author yijun
    * @date 2018/09/07 11:36:51
    */
    long pageWarehouseUserCounts(WarehouseUser warehouseUser);
}
