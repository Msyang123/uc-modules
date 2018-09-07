package com.lhiot.uc.basic.service;

import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.model.UserDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author zhangfeng created in 2018/9/7 11:23
 **/
@Service
@Slf4j
public class UserService {
    private ApplyUserMapper applyUserMapper;

    public UserService(ApplyUserMapper applyUserMapper) {
        this.applyUserMapper = applyUserMapper;
    }

    /**
     * 根据业务用户ID查询用户信息
     * @param applyUserId
     * @return
     */
    public UserDetailResult findById(Long applyUserId){
        return applyUserMapper.findById(applyUserId);
    }

    /**
     * 根据业务用户ID查询用户信息
     * @param openId
     * @return
     */
    public UserDetailResult findByopenId(String openId){
        return applyUserMapper.findByOpenId(openId);
    }

    /**
     * 根据业务用户ID查询用户信息
     * @param phone
     * @param apply
     * @return
     */
    public UserDetailResult findByPhone(String phone, Apply apply){
        ApplyUser applyUser = new ApplyUser();
        applyUser.setApply(apply);
        applyUser.setPhone(phone);
        return applyUserMapper.findByPhone(applyUser);
    }
}
