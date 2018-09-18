package com.lhiot.uc.basic.service;

import com.lhiot.uc.basic.mapper.UserAddressMapper;
import com.lhiot.uc.basic.model.UserAddress;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author zhangfeng created in 2018/9/17 16:51
 **/
@Service
@Slf4j
@Transactional
public class UserAddressService {

    private UserAddressMapper userAddressMapper;

    public UserAddressService(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    public boolean addUserAddress(UserAddress userAddress){
      return userAddressMapper.insert(userAddress) > 0 ;
    }

    public boolean updateAddress(UserAddress userAddress) {
        return userAddressMapper.updateAddress(userAddress) > 0 ;
    }

    public boolean deleteByIds(List list){
        return userAddressMapper.deleteByIds(list) > 0;
    }

    public UserAddress findById(Long id){
       return userAddressMapper.findById(id);
    }

    public List<UserAddress> findListByUserId(Long baseUserId){
        return userAddressMapper.findListByUserId(baseUserId);
    }
}
