package com.lhiot.uc.session.service;

import com.leon.microx.support.session.Session;
import com.lhiot.uc.session.model.LoginModel;
import com.lhiot.uc.session.repository.SessionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Leon (234239150@qq.com) created in 9:58 18.8.30
 */
@Service
public class SessionService {

    private final SessionMapper mapper;

    @Autowired
    public SessionService(SessionMapper mapper) {
        this.mapper = mapper;
    }

    public void online(LoginModel login){
        mapper.online(login);
    }

    public void logout(Session.User user){
        mapper.updateLastLogin(user);
    }
}
