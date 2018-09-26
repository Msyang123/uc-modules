package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.entity.UserBinding;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author zhangfeng created in 2018/9/7 15:37
 **/
@Mapper
@Repository
public interface UserBindingMapper {

    int insert(UserBinding userBinding);

    BaseUser findBaseUserByBindingRelation(String phone);
}
