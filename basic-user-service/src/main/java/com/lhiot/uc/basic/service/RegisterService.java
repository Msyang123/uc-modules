package com.lhiot.uc.basic.service;

import com.leon.microx.util.SnowflakeId;
import com.lhiot.uc.basic.model.DeviceUniqueResult;
import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.DeviceUniqueMapper;
import com.lhiot.uc.basic.mapper.UserExtensionMapper;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@Slf4j
public class RegisterService {

    private final ApplyUserMapper applyUserMapper;
    private final DeviceUniqueMapper deviceUniqueMapper;
    private final UserExtensionMapper userExtensionMapper;
    private final BaseUserMapper baseUserMapper;
    private final SnowflakeId snowflakeId;

    public RegisterService(ApplyUserMapper applyUserMapper, DeviceUniqueMapper deviceUniqueMapper, UserExtensionMapper userExtensionMapper,
                           BaseUserMapper baseUserMapper, SnowflakeId snowflakeId) {
        this.applyUserMapper = applyUserMapper;
        this.deviceUniqueMapper = deviceUniqueMapper;
        this.userExtensionMapper = userExtensionMapper;
        this.baseUserMapper = baseUserMapper;
        this.snowflakeId = snowflakeId;
    }

    /**
     * 依据设备唯一编码查找设备信息
     *
     * @param ukey 设备唯一编码
     * @return
     */
    public DeviceUniqueResult findByUkey(String ukey) {
        return  deviceUniqueMapper.findByUkey(ukey);
    }

    /**
     * 根据用户手机号查询用户编号
     *
     * @param phone。其中包含USER_MOBILE
     * @return 用户ID
     */
    public int count(Long id,String phone) {
        return applyUserMapper.count(new ApplyUser().copy(id,phone));
    }

    /**
     * 手机号码注册 添加用户信息
     *
     * @param param
     * @return
     */
    public UserDetailResult addRegisterUser(PhoneRegisterParam param) {
        UserDetailResult userRegister = new UserDetailResult();
        Long userId = snowflakeId.longId();
        userRegister.setRegistrationAt(new Timestamp(System.currentTimeMillis()));
        userRegister.setAvatar("http://resource.shuiguoshule.com.cn/user_image/2017-04-14/oGPg2MyfeUrO9knaDLyS.jpg");
        userRegister.setNickname(param.getUserMobile());
        userRegister.setSex("1");
        userRegister.setId(userId);
        userRegister.setPassword(param.getUserPassword());
        userRegister.setPhone(param.getUserMobile());
//        sqlSession.insert("t_app_user.create_user", userRegister);
        //写入用户扩展信息
        //用户信息
        userRegister.setBirthday("");
        userRegister.setRealname("");
        userRegister.setEmail("");
        userRegister.setQq("");
        userRegister.setAddress("");
        userRegister.setDescription("");
        userRegister.setApply(param.getApply());
        //用户拓展
        userRegister.setPoint(0);
        userRegister.setCurrency(0);
        userRegister.setLevel("1");

        //write by yj
        //将信息写入baseUser中并且登录
        long baseUserId = snowflakeId.longId();
        baseUserMapper.save(new BaseUser().copy(baseUserId,param));
        //将base_user_id回写用户
        userRegister.setBaseUserId(baseUserId);
        applyUserMapper.save(new ApplyUser().copy(userRegister));
//        sqlSession.insert("t_app_user.create", userRegister);

        //写拓展表
        /*RegisterPhone registerPhone = new RegisterPhone();
        registerPhone.setPhone(userRegister.getPhone());
        registerPhone.setUnionId(userRegister.getUnionId());
        commonsUserService.saveRegisterPhone(registerPhone);*/
        return userRegister;
    }
}
