package com.lhiot.uc.session.mapper;

import com.leon.microx.support.session.Sessions;
import com.lhiot.uc.session.model.LoginParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Leon (234239150@qq.com) created in 9:56 18.8.30
 */
@Mapper
@Repository
public interface SessionMapper {

    int insert(LoginParam login);

    void updateLastLogin(Sessions.User user);
}
