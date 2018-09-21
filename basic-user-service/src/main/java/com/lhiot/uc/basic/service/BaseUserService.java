package com.lhiot.uc.basic.service;

import com.leon.microx.util.SnowflakeId;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import com.lhiot.uc.basic.model.BaseUserResult;

import java.util.Objects;

/**
 * @Author zhangfeng created in 2018/9/11 11:23
 **/
public abstract class BaseUserService {

    private UserBindingMapper bindingMapper;
    private SnowflakeId snowflakeId;
    private BaseUserMapper baseUserMapper;

    protected BaseUserService(UserBindingMapper bindingMapper, SnowflakeId snowflakeId, BaseUserMapper baseUserMapper) {
        this.bindingMapper = bindingMapper;
        this.snowflakeId = snowflakeId;
        this.baseUserMapper = baseUserMapper;
    }

    /**
     * 根据手机号查询是否存在绑定关系且基础用户
     *
     * @param phone 手机号
     * @return
     */
    protected BaseUser findBaseUserByBindingRelation(String phone) {
        BaseUser baseUser = bindingMapper.findBaseUserByBindingRelation(phone);
        if (Objects.isNull(baseUser)) {
            baseUser = new BaseUser();
            baseUser.setPhone(phone);
            baseUser.setId(snowflakeId.longId());
            baseUserMapper.insert(baseUser);
        }
        return baseUser;
    }
}
