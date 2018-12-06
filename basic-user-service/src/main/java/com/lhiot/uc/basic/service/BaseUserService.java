package com.lhiot.uc.basic.service;

import com.leon.microx.id.Generator;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import lombok.Getter;

import java.util.Objects;

/**
 * @author zhangfeng created in 2018/9/11 11:23
 **/
public abstract class BaseUserService {

    private UserBindingMapper bindingMapper;
    @Getter
    private Generator<Long> generator;
    private BaseUserMapper baseUserMapper;

    protected BaseUserService(UserBindingMapper bindingMapper, Generator<Long> generator, BaseUserMapper baseUserMapper) {
        this.bindingMapper = bindingMapper;
        this.generator = generator;
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
            baseUser.setId(generator.get());
            baseUserMapper.insert(baseUser);
        }
        return baseUser;
    }
}
