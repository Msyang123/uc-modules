package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.ApplyUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ApplyUserMapper {

    int count(ApplyUser param);

    int save(ApplyUser param);

}
