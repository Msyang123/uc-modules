package com.lhiot.uc.basic.api;

import com.leon.microx.support.result.Multiple;
import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.basic.entity.Apply;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.model.ModificationUserParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @Author zhangfeng created in 2018/9/7 11:15
 **/
@RestController
@Slf4j
@RequestMapping("/users")
public class UserApi {

    private final UserService userService;

    public UserApi(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation("根据用户ID查询用户信息")
    @ApiImplicitParam(paramType = "path",name = "userId",value = "用户ID",dataType = "Long",required = true)
    @GetMapping("user-id/{userId}")
    public ResponseEntity findById(@PathVariable Long userId){
       UserDetailResult user =  userService.findById(userId);
       if (Objects.equals(user,null)){
           return ResponseEntity.badRequest().body("该用户不存在！");
       }
        return ResponseEntity.ok(user);
    }

    @ApiOperation("根据用户OpenID查询用户信息")
    @ApiImplicitParam(paramType = "path",name = "openId",value = "用户ID",dataType = "Long",required = true)
    @GetMapping("open-id/{openId}")
    public ResponseEntity findByOpenId(@PathVariable String openId){
        UserDetailResult user =  userService.findByopenId(openId);
        if (Objects.equals(user,null)){
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation("根据业务手机号码查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "phoneNumber",value = "用户ID",dataType = "Long",required = true),
            @ApiImplicitParam(paramType = "query",name = "apply",value = "应用类型",dataType = "Apply",required = true)
    })
    @GetMapping("phone/{phoneNumber}")
    public ResponseEntity findByPhone(@PathVariable String phoneNumber, @RequestParam Apply apply){
        UserDetailResult user =  userService.findByPhone(phoneNumber,apply);
        if (Objects.equals(user,null)){
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据用户ID组成的字符串集合查询用户信息",response = UserDetailResult.class, responseContainer = "Multiple")
    @ApiImplicitParam(paramType = "body", name = "ids", dataType = "string", required = true, value = "用户id字符串结合")
    @PostMapping("/list/ids")
    public ResponseEntity findUserList(@RequestBody String ids) {
        return ResponseEntity.ok(userService.findUsersByIds(ids));
    }

    @ApiOperation(value = "根据用户手机号码组成的字符串集合查询用户信息",response = UserDetailResult.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = "body", name = "phones", dataType = "string", required = true, value = "用户手机号码字符串结合")
    @PostMapping("/list/phones")
    public ResponseEntity findUserListByPhone(@RequestBody String phones) {
        return ResponseEntity.ok(userService.findUsersByPhones(phones));
    }

    @ApiOperation(value = "根据关键字搜索用户（关键字模糊匹配手机号码和昵称）",response = ApplyUser.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = "String", name = "keyword", dataType = "string", required = true, value = "关键字")
    @GetMapping("/{keyword}")
    public ResponseEntity findUserByKeyword(@PathVariable String keyword) {
        return ResponseEntity.ok(userService.findByKeyword(keyword));
    }

    @ApiOperation(value = "根据业务用户ID修改用户信息", notes = "根据业务用户ID修改用户信息！")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "传入参数", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "body", name = "param", value = "传入参数", required = true, dataType = "ModificationUserParam")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity updateInfo(@NotNull @PathVariable("id") Long userId, @RequestBody ModificationUserParam param){
       if (!userService.countById(userId)){
           return ResponseEntity.badRequest().body("用户不存在！");
       }
       ApplyUser applyUser = new ApplyUser();
        BeanUtils.of(applyUser).populate(param);
        applyUser.setId(userId);
       if (! userService.updateUserById(applyUser)){
           return ResponseEntity.badRequest().body("更新用户信息失败！");
       }
       return ResponseEntity.ok().build();
    }

}
