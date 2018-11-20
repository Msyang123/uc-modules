package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.model.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangfeng created in 2018/9/17 16:08
 **/
@Mapper
@Repository
public interface UserAddressMapper {

    int insert(UserAddress userAddress);

    int updateAddress(UserAddress userAddress);

    int deleteByIds(List list);

    UserAddress findById(Long id);

    List<UserAddress> findListByUserId(Long baseUserId);
}
