package com.lhiot.uc.basic.service;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.SnowflakeId;
import com.leon.microx.util.StringUtils;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.entity.UserBinding;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.UserBindingMapper;
import com.lhiot.uc.basic.model.UserBindingPhoneParam;
import com.lhiot.uc.basic.model.UserBindingWeChatParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class UserBindingService extends BaseUserService {

    private ApplyUserMapper applyUserMapper;
    private UserBindingMapper userBindingMapper;

    public UserBindingService(ApplyUserMapper applyUserMapper, BaseUserMapper baseUserMapper, SnowflakeId snowflakeId, UserBindingMapper userBindingMapper) {
        super(userBindingMapper, snowflakeId, baseUserMapper);
        this.applyUserMapper = applyUserMapper;
        this.userBindingMapper = userBindingMapper;
    }

    /**
     * 判断手机号是否绑定微信
     *
     * @param applyUser
     * @return
     */
    public boolean isBindingWeChat(ApplyUser applyUser) {
        String openId = applyUserMapper.findOpenIdByPhone(applyUser);
        return StringUtils.isNotBlank(openId);
    }

    /**
     * 判断微信注册用户是否绑定手机号
     *
     * @param openId
     * @return
     */
    public boolean isBindingPhone(String openId) {
        String phone = applyUserMapper.findPhoneByOpenId(openId);
        return StringUtils.isNotBlank(phone);
    }


    /**
     * 微信注册用户绑定基础用户
     *
     * @param param
     * @return
     */
    public UserDetailResult bindingPhone(UserBindingPhoneParam param) {
        BaseUser baseUser = this.findBaseUserByBindingRelation(param.getPhone());

        UserBinding userBinding = new UserBinding();
        userBinding.setPhone(param.getPhone());
        userBinding.setBaseUserId(baseUser.getId());
        userBinding.setApplyUserId(param.getApplyUserId());
        userBindingMapper.save(userBinding);

        ApplyUser applyUser = new ApplyUser();
        applyUser.setBaseUserId(baseUser.getId());
        applyUser.setId(param.getApplyUserId());
        applyUser.setPhone(param.getPhone());
        applyUserMapper.updateByBindPhone(applyUser);

        UserDetailResult userDetailResult = new UserDetailResult();
        BeanUtils.of(userDetailResult).populate(applyUser);
        userDetailResult.setRealName(baseUser.getRealName());
        userDetailResult.setCurrency(baseUser.getCurrency());
        userDetailResult.setPoint(baseUser.getMemberPoints());
        return userDetailResult;
    }

    public boolean bindingWeChat(UserBindingWeChatParam param) {
        ApplyUser user = new ApplyUser();
        user.setOpenId(param.getOpenId());
        user.setUnionId(param.getUnionId());
        user.setId(param.getApplyUserId());
        applyUserMapper.updateWeChatInfoById(user);
        return applyUserMapper.updateWeChatInfoById(user) > 0;
    }

    public boolean removingWeChatBinding(Long userId) {
        return applyUserMapper.updateWeChatBinding(userId) > 0;
    }
}
