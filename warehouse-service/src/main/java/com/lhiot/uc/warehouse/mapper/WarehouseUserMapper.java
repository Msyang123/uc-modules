package com.lhiot.uc.warehouse.mapper;

import com.lhiot.uc.warehouse.domain.entity.WarehouseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* Description:用户仓库Mapper类
* @author yijun
* @date 2018/09/07
*/
@Mapper
@Repository
public interface WarehouseUserMapper {

    /**
    * Description:新增用户仓库
    *
    * @param warehouseUser WarehouseUser
    * @return int
    */
    int add(WarehouseUser warehouseUser);

    /**
    * Description:根据id修改用户仓库
    *
    * @param warehouseUser WarehouseUser
    * @return int
    */
    int updateById(WarehouseUser warehouseUser);

    /**
    * Description:根据ids删除用户仓库
    *
    * @param ids List<String>
    * @return int
    */
    int deleteByIds(List<String> ids);

    /**
    * Description:根据id查找用户仓库
    *
    * @param id Long
    * @return WarehouseUser
    */
    WarehouseUser selectById(Long id);


    /**
     * Description:根据基础用户id查找用户仓库
     *
     * @param baseUserId Long
     * @return WarehouseUser
     */
    WarehouseUser findByBaseUserId(Long baseUserId);


    /**
    * Description:查询用户仓库列表
    *
    * @param warehouseUser WarehouseUser
    * @return List<WarehouseUser>
    */
     List<WarehouseUser> pageWarehouseUsers(WarehouseUser warehouseUser);


    /**
    * Description: 查询用户仓库总记录数
    *
    * @param warehouseUser WarehouseUser
    * @return long
    */
    long pageWarehouseUserCounts(WarehouseUser warehouseUser);
}
