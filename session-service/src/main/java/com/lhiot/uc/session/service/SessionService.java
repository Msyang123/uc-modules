package com.lhiot.uc.session.service;

import com.leon.microx.support.session.Authority;
import com.leon.microx.support.session.Sessions;
import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.session.mapper.SessionMapper;
import com.lhiot.uc.session.model.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Leon (234239150@qq.com) created in 9:58 18.8.30
 */
@Service
public class SessionService {

    private final SessionMapper mapper;
    private final Sessions session;

    @Autowired
    public SessionService(SessionMapper mapper, Sessions session) {
        this.mapper = mapper;
        this.session = session;
    }

    public void online(LoginParam login) {
        mapper.insert(login);
    }

    /**
     * 用户登录生成sessionId并缓存
     *
     * @param object Object 需要被缓存的信息
     * @param request HttpServletRequest
     * @return String
     */
    public String createSession(Object object, HttpServletRequest request) {
        Map<String, Object> map = BeanUtils.of(object).toMap();
        Sessions.User sessionUser = Sessions.create(request).user(map).timeToLive(30, TimeUnit.MINUTES).authorities(Authority.of("**", RequestMethod.values()));
        sessionUser.addAttribute("loginAt", Instant.now());
        // 缓存session
        return session.cache(sessionUser);
    }

    public void logout(Sessions.User user) {
        mapper.updateLastLogin(user);
    }
}
