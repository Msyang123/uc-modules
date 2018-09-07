package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.UserBinding;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangfeng created in 2018/9/7 15:37
 **/
@Mapper
@Repository
public interface UserBindingMapper {

    Long findByPhone(String phone);

    int save(UserBinding userBinding);
}
