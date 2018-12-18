package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.LockStatus;
import com.lhiot.uc.basic.entity.SwitchStatus;
import com.lhiot.uc.basic.model.PhoneAndPasswordSearchParam;
import com.lhiot.uc.basic.model.QuerySearch;
import com.lhiot.uc.basic.model.UserDetailResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ApplyUserMapper {

    int countByPhoneNumber(ApplyUser user);

    int countByOpenId(String openId);

    int countById(Long id);

    int insert(ApplyUser param);

    /**
     * 根据用户ID跟支付密码查询用户支付权限
     *
     * @param map id和支付密码
     * @return
     */
    SwitchStatus findPermissionsByIdAndPaymentPassword(Map<String, Object> map);

    ApplyUser findPaymentPasswordById(Long userId);

    UserDetailResult findById(Long userId);

    UserDetailResult findByOpenId(String openId);

    UserDetailResult findByPhone(ApplyUser applyUser);

    List<UserDetailResult> findByIdList(List<String> ids);

    List<UserDetailResult> findByPhoneList(List<String> ids);

    List<UserDetailResult> findByKeyword(String keyword);

    UserDetailResult findByPhoneAndPassword(PhoneAndPasswordSearchParam param);

    int updateUserById(ApplyUser user);

    int updateByBindPhone(ApplyUser user);

    String findOpenIdByPhone(ApplyUser user);

    String findPhoneByOpenId(String openId);

    int updateWeChatInfoById(ApplyUser user);

    Long findIdByPhoneNumber(ApplyUser user);

    int updateWeChatBinding(Long userId);

    int updatePasswordById(ApplyUser user);

    int updatePaymentPermissionsById(ApplyUser user);

    int updateUnionIdByUserId(ApplyUser user);

    int countByIdAndPassword(ApplyUser user);

    List<UserDetailResult> findQuery(QuerySearch param);

    int countByQuery(QuerySearch param);

    int updateLockStatus(Map<String, Object> map);

    Long findBaseUserId(Long userId);

    List<String> findIdsByPhone(String phone);
}
