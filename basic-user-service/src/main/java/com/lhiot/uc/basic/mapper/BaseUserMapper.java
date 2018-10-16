package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.model.BaseUserResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Mapper
@Repository
public interface BaseUserMapper {

    /**
     * 添加基础用户表
     *
     * @param param BaseUser
     * @return Long
     */
    Long insert(BaseUser param);

    int updateCurrencyByApplyUserIdForSub(Map<String, Object> map);

    int updateCurrencyByApplyUserIdForAdd(Map<String, Object> map);

    Map<String,Object> findPaymentPermissionsByApplyUserId(Long userId);

    Long findCurrencyByApplyUserId(Long userId);

    BaseUserResult findById(Long id);

    /**
     * 根据基础用户Id添加鲜果币
     * @param map id 基础用户Id money 添加金额
     */
    void updateBalanceByIdForAdd(Map<String,Object> map);
}
