package com.lhiot.uc.session.api;

import com.leon.microx.support.session.Session;
import com.lhiot.uc.session.model.LoginModel;
import com.lhiot.uc.session.service.SessionService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * @author Leon (234239150@qq.com) created in 9:35 18.8.30
 */
@Slf4j
@RestController
public class SessionApi {

    private final Session session;
    private SessionService service;

    @Autowired
    public SessionApi(Session session, SessionService service) {
        this.session = session;
        this.service = service;
    }

    @Session.Uncheck
    @ApiOperation("手机登录")
    @PostMapping("/cellphone/session")
    public ResponseEntity cellphoneLogin(LoginModel model, @ApiIgnore HttpServletRequest request) {
        // TODO Feign调用基础服务 查询用户信息：用户ID、用户名、可以访问的API集合（ANT匹配）

        // 创建session
        Session.User sessionUser = Session.User.of(request).user("1", "leon").timeToLive(30, TimeUnit.MINUTES).antPaths("**");
        sessionUser.put("loginAt", Instant.now());
        // 缓存session
        String sessionId = session.cache(sessionUser);
        try {
            // 返回session
            return ResponseEntity.created(URI.create("/cellphone/session/" + sessionId))
                    .header(Session.HTTP_HEADER_NAME, sessionId)
                    .build();
        }finally {
            // 更新登录状态
            service.online(model);
        }
    }

    @ApiOperation("退出登录")
    @DeleteMapping("/session")
    public ResponseEntity logout(Session.User current, @ApiIgnore HttpServletRequest request) {
        String sessionId = Session.id(request);
        session.invalidate(sessionId);
        service.logout(current);
        log.info("user {} logout.", current);
        return ResponseEntity.noContent().build();
    }
}
