package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.model.BaseUserResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface BaseUserMapper {

    /**
     * 添加基础用户表
     *
     * @param param
     * @return
     */
    Long insert(BaseUser param);

    int updateCurrencyByIdForSub(Map<String, Object> map);

    int updateCurrencyByIdForAdd(Map<String, Object> map);

    Long findCurrencyById(Long id);

    BaseUserResult findById(Long id);
}
