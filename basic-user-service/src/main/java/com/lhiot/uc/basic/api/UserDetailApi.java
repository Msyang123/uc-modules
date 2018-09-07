package com.lhiot.uc.basic.api;

import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @Author zhangfeng created in 2018/9/7 11:15
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserDetailApi {

    private final UserService userService;

    public UserDetailApi(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("根据用户ID查询用户信息")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用户ID",dataType = "Long",required = true)
    @RequestMapping("user-id/{userId}")
    public ResponseEntity findById(@PathVariable Long userId){
       UserDetailResult user =  userService.findById(userId);
       if (Objects.equals(user,null)){
           return ResponseEntity.badRequest().body("该用户不存在！");
       }
        return ResponseEntity.ok(user);
    }

    @ApiOperation("根据用户OpenID查询用户信息")
    @ApiImplicitParam(paramType = "path",name = "openId",value = "用户ID",dataType = "Long",required = true)
    @RequestMapping("open-id/{openId}")
    public ResponseEntity findByOpenId(@PathVariable String openId){
        UserDetailResult user =  userService.findByopenId(openId);
        if (Objects.equals(user,null)){
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation("根据业务手机号码查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "phone",value = "用户ID",dataType = "Long",required = true),
            @ApiImplicitParam(paramType = "query",name = "apply",value = "应用类型",dataType = "Apply",required = true)
    })
    @RequestMapping("phone/{phone}")
    public ResponseEntity findByPhone(@PathVariable String phone, @RequestParam Apply apply){
        UserDetailResult user =  userService.findByPhone(phone,apply);
        if (Objects.equals(user,null)){
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }
}
