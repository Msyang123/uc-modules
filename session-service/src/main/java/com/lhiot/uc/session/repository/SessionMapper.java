package com.lhiot.uc.session.repository;

import com.leon.microx.support.session.Session;
import com.lhiot.uc.session.model.LoginModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * @author Leon (234239150@qq.com) created in 9:56 18.8.30
 */
@Mapper
@Repository
public interface SessionMapper {

    void online(LoginModel login);

    void updateLastLogin(Session.User user);
}
