package com.lhiot.uc.basic.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.SnowflakeId;
import com.leon.microx.util.StringUtils;
import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.entity.UserBinding;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.model.WeChatRegisterParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@Transactional
public class RegisterService extends BaseUserService{

    private final ApplyUserMapper applyUserMapper;
    private final SnowflakeId snowflakeId;
    private final UserBindingMapper userBindingMapper;

    public RegisterService(ApplyUserMapper applyUserMapper, BaseUserMapper baseUserMapper, SnowflakeId snowflakeId, UserBindingMapper userBindingMapper) {
        super(userBindingMapper, snowflakeId, baseUserMapper);
        this.applyUserMapper = applyUserMapper;
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
    public UserDetailResult register(PhoneRegisterParam param) {

        BaseUser baseUser = this.findBaseUserByBindingRelation(param.getPhone());

        ApplyUser applyUser = new ApplyUser();
        applyUser.setId(snowflakeId.longId());
        applyUser.setNickname(param.getPhone().replace(param.getPhone().substring(3, 7), "xxxx"));
        BeanUtils.of(applyUser).populate(param);
        applyUser.setBaseUserId(baseUser.getId());
        applyUserMapper.insert(applyUser);

        UserBinding userBinding = new UserBinding();
        userBinding.setBaseUserId(baseUser.getId());
        userBinding.setApplyUserId(applyUser.getId());
        userBinding.setPhone(param.getPhone());
        userBindingMapper.save(userBinding);

        UserDetailResult result = new UserDetailResult();
        BeanUtils.of(result).populate(applyUser);
        result.setCurrency(baseUser.getCurrency());
        result.setRealName(baseUser.getRealName());

        return result;
    }

    /**
     * 微信注册，写入用户信息,当入参phone不为空时，则添加baseUser信息
     *
     * @param param
     * @return
     */
    public UserDetailResult registerByOpenId(WeChatRegisterParam param) {

        ApplyUser applyUser = new ApplyUser();
        applyUser.setId(snowflakeId.longId());
        BeanUtils.of(applyUser).populate(param);

        BaseUser baseUser = null;
        if (StringUtils.isNotBlank(param.getPhone())) {
            //在该应用中，手机号不存在注册用户，则添加应用用户信息
            Long userId = this.findIdByPhone(param);
            //基础用户信息存在该手机号用户则只添加绑定信息，若不存在则添加基础用户信息
            baseUser = this.findBaseUserByBindingRelation(param.getPhone());
            if (Objects.isNull(userId)){
                applyUser.setBaseUserId(baseUser.getId());

                UserBinding userBinding = new UserBinding();
                userBinding.setBaseUserId(baseUser.getId());
                userBinding.setApplyUserId(applyUser.getId());
                userBinding.setPhone(param.getPhone());
                userBindingMapper.save(userBinding);
                applyUserMapper.insert(applyUser);
            }else{
                //手机号已存在注册用户，则只进行修改
                applyUser.setOpenId(param.getOpenId());
                applyUser.setUnionId(param.getUnionId());
                applyUser.setId(userId);
                applyUserMapper.updateWeChatInfoById(applyUser);
            }
        }else {
            applyUserMapper.insert(applyUser);
            baseUser = new BaseUser();
        }

        UserDetailResult result = new UserDetailResult();
        result.setCurrency(baseUser.getCurrency());
        result.setPoint(baseUser.getMemberPoints());
        result.setRealName(baseUser.getRealName());
        BeanUtils.of(result).populate(applyUser);
        return result;
    }

    public Long findIdByPhone(WeChatRegisterParam param){
        ApplyUser user = new ApplyUser();
        user.setPhone(param.getPhone());
        user.setApply(param.getApply());
        return applyUserMapper.findIdByPhoneNumber(user);
    }
}
