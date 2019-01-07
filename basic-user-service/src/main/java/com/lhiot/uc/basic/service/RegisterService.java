package com.lhiot.uc.basic.service;

import com.leon.microx.id.Generator;
import com.leon.microx.util.Beans;
import com.leon.microx.util.StringUtils;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.entity.UserBinding;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.model.WeChatRegisterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@Transactional
public class RegisterService extends BaseUserService {

    private final ApplyUserMapper applyUserMapper;
    private final UserBindingMapper userBindingMapper;

    public RegisterService(ApplyUserMapper applyUserMapper, BaseUserMapper baseUserMapper, Generator<Long> generator, UserBindingMapper userBindingMapper) {
        super(userBindingMapper, generator, baseUserMapper);
        this.applyUserMapper = applyUserMapper;
        this.userBindingMapper = userBindingMapper;
    }

    /**
     * 手机号是否已注册
     *
     * @param phone           String 手机号
     * @param applicationType String
     * @return boolean
     */
    public boolean hasPhone(String phone, String applicationType) {
        ApplyUser user = new ApplyUser();
        user.setPhone(phone);
        user.setApplicationType(applicationType);
        return applyUserMapper.countByPhoneNumber(user) > 0;
    }

    /**
     * openId是否注册
     *
     * @param openId String
     * @return boolean
     */
    public boolean hasOpenId(String openId) {
        return applyUserMapper.countByOpenId(openId) > 0;
    }

    /**
     * 手机号码注册 添加用户信息
     *
     * @param param PhoneRegisterParam
     * @return UserDetailResult
     */
    public UserDetailResult register(PhoneRegisterParam param) {

        BaseUser baseUser = this.findBaseUserByBindingRelation(param.getPhone());

        ApplyUser applyUser = Beans.from(param).populate(ApplyUser::new);
        applyUser.setId(getGenerator().get());
        applyUser.setNickname(param.getPhone().replace(param.getPhone().substring(3, 7), "xxxx"));
        applyUser.setBaseUserId(baseUser.getId());
        applyUserMapper.insert(applyUser);

        UserBinding userBinding = new UserBinding();
        userBinding.setBaseUserId(baseUser.getId());
        userBinding.setApplyUserId(applyUser.getId());
        userBinding.setPhone(param.getPhone());
        userBindingMapper.insert(userBinding);

        UserDetailResult result = Beans.from(applyUser).populate(UserDetailResult::new);
        result.setBalance(baseUser.getBalance());
        result.setRealName(baseUser.getRealName());

        return result;
    }

    /**
     * 微信注册，写入用户信息,当入参phone不为空时，则添加baseUser信息
     *
     * @param param WeChatRegisterParam
     * @return UserDetailResult
     */
    public UserDetailResult registerByOpenId(WeChatRegisterParam param) {

        ApplyUser applyUser = Beans.from(param).populate(ApplyUser::new);
        applyUser.setId(getGenerator().get());

        BaseUser baseUser;
        if (StringUtils.isNotBlank(param.getPhone())) {
            //在该应用中，手机号不存在注册用户，则添加应用用户信息
            Long userId = this.findIdByPhone(param);
            //基础用户信息存在该手机号用户则只添加绑定信息，若不存在则添加基础用户信息
            baseUser = this.findBaseUserByBindingRelation(param.getPhone());
            if (Objects.isNull(userId)) {
                applyUser.setBaseUserId(baseUser.getId());

                UserBinding userBinding = new UserBinding();
                userBinding.setBaseUserId(baseUser.getId());
                userBinding.setApplyUserId(applyUser.getId());
                userBinding.setPhone(param.getPhone());
                userBindingMapper.insert(userBinding);
                applyUserMapper.insert(applyUser);
            } else {
                //手机号已存在注册用户，则只进行修改
                applyUser.setOpenId(param.getOpenId());
                applyUser.setUnionId(param.getUnionId());
                applyUser.setId(userId);
                applyUserMapper.updateWeChatInfoById(applyUser);
            }
        } else {
            applyUserMapper.insert(applyUser);
            baseUser = new BaseUser();
        }

        UserDetailResult result = Beans.from(applyUser).populate(UserDetailResult::new);
        result.setBalance(baseUser.getBalance());
        result.setPoint(baseUser.getMemberPoints());
        result.setRealName(baseUser.getRealName());
        return result;
    }

    private Long findIdByPhone(WeChatRegisterParam param) {
        ApplyUser user = new ApplyUser();
        user.setPhone(param.getPhone());
        user.setApplicationType(param.getApplicationType());
        return applyUserMapper.findIdByPhoneNumber(user);
    }
}
