package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BaseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BaseUserMapper {

    /**
     * 添加基础用户表
     * @param param
     * @return
     */
    int save(BaseUser param);

}
