package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BaseUser;
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

    int updateCurrencyByIdForAdd(BaseUser user);

    Long findCurrencyById(Long id);
}
