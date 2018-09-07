package com.lhiot.uc.basic.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.SnowflakeId;
import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.entity.UserBinding;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserBindingParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.model.WechatRegisterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class RegisterService {

    private final ApplyUserMapper applyUserMapper;
    private final BaseUserMapper baseUserMapper;
    private final SnowflakeId snowflakeId;
    private final UserBindingMapper userBindingMapper;

    public RegisterService(ApplyUserMapper applyUserMapper, BaseUserMapper baseUserMapper, SnowflakeId snowflakeId, UserBindingMapper userBindingMapper) {
        this.applyUserMapper = applyUserMapper;
        this.baseUserMapper = baseUserMapper;
        this.snowflakeId = snowflakeId;
        this.userBindingMapper = userBindingMapper;
    }

    /**
     * 手机号是否已注册
     *
     * @param phone 手机号
     * @return
     */
    public boolean hasPhone(String phone, Apply apply) {
        ApplyUser user = new ApplyUser();
        user.setPhone(phone);
        user.setApply(apply);
        return applyUserMapper.countByPhoneNumber(user) > 0;
    }

    /**
     * openId是否注册
     *
     * @param openId
     * @return
     */
    public boolean hasOpenId(String openId) {
        return applyUserMapper.countByOpenId(openId) > 0;
    }

    /**
     * 手机号码注册 添加用户信息
     *
     * @param param
     * @return
     */
    @Transactional
    public UserDetailResult register(PhoneRegisterParam param) {

        Long baseUserId = userBindingMapper.findByPhone(param.getPhone());
        BaseUser baseUser = new BaseUser();
        if (Objects.equals(baseUserId, null)) {
            baseUserId = snowflakeId.longId();
            baseUser.setId(baseUserId);
            baseUser.setPhone(param.getPhone());
            baseUserMapper.save(baseUser);
        }

        ApplyUser applyUser = new ApplyUser();
        applyUser.setId(snowflakeId.longId());
        BeanUtils.of(applyUser).populate(param);
        applyUser.setBaseUserId(baseUserId);
        applyUserMapper.save(applyUser);

        UserBinding userBinding = new UserBinding();
        userBinding.setBaseUserId(baseUserId);
        userBinding.setApplyUserId(applyUser.getId());
        userBinding.setPhone(param.getPhone());
        userBindingMapper.save(userBinding);

        UserDetailResult result = new UserDetailResult();
        BeanUtils.of(result).populate(applyUser);
        result.setCurrency(baseUser.getCurrency());
        return result;
    }

    /**
     * 微信注册，写入用户信息
     *
     * @param param
     * @return
     */
    public UserDetailResult registerByOpenId(WechatRegisterParam param) {
        ApplyUser applyUser = new ApplyUser();
        applyUser.setId(snowflakeId.longId());
        BeanUtils.of(applyUser).populate(param);
        applyUserMapper.save(applyUser);

        UserDetailResult result = new UserDetailResult();
        BeanUtils.of(result).populate(applyUser);
        return result;
    }

    /**
     * 根据手机号码查找是否存在绑定关系
     *
     * @param phone
     * @return
     */
    public Long hasBinding(String phone) {
        return userBindingMapper.findByPhone(phone);
    }


    /** 绑定基础用户
     * @param param
     * @return
     */
    public boolean binding(UserBindingParam param) {
        Long baseUserId = userBindingMapper.findByPhone(param.getPhone());
        if (Objects.equals(baseUserId, null)) {
            this.save(param.getPhone());
        }

        UserBinding userBinding = new UserBinding();
        userBinding.setPhone(param.getPhone());
        userBinding.setBaseUserId(baseUserId);
        userBinding.setApplyUserId(param.getApplyUserId());
        return userBindingMapper.save(userBinding) > 0 ? true : false;
    }

    private BaseUser save(String phone) {
        BaseUser baseUser = new BaseUser();
        baseUser.setPhone(phone);
        baseUser.setId(snowflakeId.longId());
        baseUserMapper.save(baseUser);
        return baseUser;
    }

}
