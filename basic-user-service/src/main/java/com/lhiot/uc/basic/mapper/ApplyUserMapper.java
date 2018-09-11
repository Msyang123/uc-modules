package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.model.UserDetailResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ApplyUserMapper {

    int countByPhoneNumber(ApplyUser user);

    int countByOpenId(String openId);

    int countById(Long id);

    int insert(ApplyUser param);

    UserDetailResult findById(Long userId);

    UserDetailResult findByOpenId(String openId);

    UserDetailResult findByPhone(ApplyUser applyUser);

    List<UserDetailResult> findByIdList(List<String> ids);

    List<UserDetailResult> findByPhoneList(List<String> ids);

    List<ApplyUser> findByKeyword(String keyword);

    int updateUserById(ApplyUser user);

    int updateByBindPhone(ApplyUser user);

    String findOpenIdByPhone(ApplyUser user);

    String findPhoneByOpenId(String openId);

    int updateWeChatInfoById(ApplyUser user);

    Long findIdByPhoneNumber(ApplyUser user);

    int updateWeChatBinding(Long userId);
}
