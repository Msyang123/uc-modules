package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.model.UserDetailResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ApplyUserMapper {

    int countByPhoneNumber(ApplyUser user);

    int countByOpenId(String openId);
    int save(ApplyUser param);

    UserDetailResult findById(Long userId);
    UserDetailResult findByOpenId(String openId);
    UserDetailResult findByPhone(ApplyUser applyUser);
}
