package com.lhiot.uc.warehouse.service;

import com.leon.microx.support.result.Pages;
import com.lhiot.uc.warehouse.entity.WarehouseUser;
import com.lhiot.uc.warehouse.mapper.WarehouseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Description:用户仓库服务类
 *
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
     * @param warehouseUser WarehouseUser
     * @return int
     */
    public int add(WarehouseUser warehouseUser) {
        return this.warehouseUserMapper.add(warehouseUser);
    }

    /**
     * Description:根据id修改用户仓库
     *
     * @param warehouseUser WarehouseUser
     * @return int
     */
    public int updateById(WarehouseUser warehouseUser) {
        return this.warehouseUserMapper.updateById(warehouseUser);
    }

    /**
     * Description:根据ids删除用户仓库
     *
     * @param ids String
     * @return int
     */
    public int deleteByIds(String ids) {
        return this.warehouseUserMapper.deleteByIds(Arrays.asList(ids.split(",")));
    }

    /**
     * Description:根据id查找用户仓库
     *
     * @param id Long
     * @return WarehouseUser
     */
    public WarehouseUser selectById(Long id) {
        return this.warehouseUserMapper.selectById(id);
    }

    /**
     * Description:根据id查找用户仓库
     *
     * @param baseUserId Long
     * @return WarehouseUser
     */
    public WarehouseUser findByBaseUserId(Long baseUserId) {
        return this.warehouseUserMapper.findByBaseUserId(baseUserId);
    }

    /**
     * Description: 查询用户仓库分页列表
     *
     * @param warehouseUser WarehouseUser
     * @return Pair
     */
    public Pages<WarehouseUser> warehouseUserPageList(WarehouseUser warehouseUser) {
        return Pages.of(
                this.warehouseUserMapper.pageWarehouseUserCounts(warehouseUser),
                this.warehouseUserList(warehouseUser)
        );
    }

    /**
     * 查询用户仓库
     * @param warehouseUser
     * @return List
     */
    public List<WarehouseUser> warehouseUserList(WarehouseUser warehouseUser){
        return this.warehouseUserMapper.pageWarehouseUsers(warehouseUser);
    }
}

