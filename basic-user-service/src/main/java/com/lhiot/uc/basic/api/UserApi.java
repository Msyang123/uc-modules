package com.lhiot.uc.basic.api;

import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.basic.entity.ApplicationType;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.SwitchStatus;
import com.lhiot.uc.basic.model.ModificationUserParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @ApiOperation(value = "根据用户ID查询用户信息", response = UserDetailResult.class)
    @ApiImplicitParam(paramType = "path", name = "userId", value = "用户ID", dataType = "Long", required = true)
    @GetMapping("/user-id/{userId}")
    public ResponseEntity findById(@PathVariable Long userId) {
        UserDetailResult user = userService.findById(userId);
        if (Objects.equals(user, null)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据用户OpenID查询用户信息", response = UserDetailResult.class)
    @ApiImplicitParam(paramType = "path", name = "openId", value = "用户ID", dataType = "Long", required = true)
    @GetMapping("/open-id/{openId}")
    public ResponseEntity findByOpenId(@PathVariable String openId) {
        UserDetailResult user = userService.findByOpenId(openId);
        if (Objects.equals(user, null)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据业务手机号码查询用户信息", response = UserDetailResult.class)
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "phoneNumber", value = "用户ID", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "apply", value = "应用类型", dataTypeClass = ApplicationType.class, required = true)
    })
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity findByPhone(@PathVariable("phoneNumber") String phoneNumber, @RequestParam("apply") ApplicationType applicationType) {
        UserDetailResult user = userService.findByPhone(phoneNumber, applicationType);
        if (Objects.equals(user, null)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据用户ID组成的字符串集合查询用户信息", response = UserDetailResult.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = "body", name = "ids", dataType = "string", required = true, value = "用户id字符串结合")
    @PostMapping("/list/ids")
    public ResponseEntity findUserList(@RequestBody String ids) {
        return ResponseEntity.ok(userService.findUsersByIds(ids));
    }

    @ApiOperation(value = "根据用户手机号码组成的字符串集合查询用户信息", response = UserDetailResult.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = "body", name = "phones", dataType = "string", required = true, value = "用户手机号码字符串结合")
    @PostMapping("/list/phones")
    public ResponseEntity findUserListByPhone(@RequestBody String phones) {
        return ResponseEntity.ok(userService.findUsersByPhones(phones));
    }

    @ApiOperation(value = "根据关键字搜索用户（关键字模糊匹配手机号码和昵称）", response = ApplyUser.class, responseContainer = "Set")
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
    public ResponseEntity updateInfo(@NotNull @PathVariable("id") Long userId, @RequestBody ModificationUserParam param) {
        if (!userService.countById(userId)) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        ApplyUser applyUser = new ApplyUser();
        BeanUtils.of(applyUser).populate(param);
        applyUser.setId(userId);
        if (!userService.updateUserById(applyUser)) {
            return ResponseEntity.badRequest().body("更新用户信息失败！");
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation("修改用户登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "登录密码", dataTypeClass = String.class, required = true)
    })
    @PutMapping("/password/{id}")
    public ResponseEntity updatePassword(@PathVariable("id") Long userId, @RequestParam String password) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setPassword(password);
        return userService.updatePasswordById(user) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改密码失败！");
    }

    @ApiOperation("修改用户支付密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(paramType = "query", name = "paymentPassword", value = "支付密码", dataTypeClass = String.class, required = true)
    })
    @PutMapping("/payment-password/{id}")
    public ResponseEntity updatePaymentPassword(@PathVariable("id") Long userId, @RequestParam String paymentPassword) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setPaymentPassword(paymentPassword);
        return userService.updatePaymentPasswordById(user) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改支付密码失败！");
    }

    @ApiOperation("修改用户鲜果币支付免密权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(paramType = "query", name = "paymentPermissions", value = "免密权限", dataTypeClass = SwitchStatus.class, required = true)
    })
    @PutMapping("/payment-permissions/{id}")
    public ResponseEntity updatePaymentPassword(@PathVariable("id") Long userId, @RequestParam SwitchStatus paymentPermissions) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setPaymentPermissions(paymentPermissions);
        return userService.updatePaymentPermissionsById(user) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改免密权限失败失败！");
    }

    @ApiOperation("判断登录密码是否正确")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "用户登录密码", dataType = "String", required = true)
    })
    @GetMapping("/password/{id}")
    public ResponseEntity determineLoginPassword(@PathVariable("id") Long userId, @RequestParam String password) {
        ApplyUser applyUser = new ApplyUser();
        applyUser.setPassword(password);
        applyUser.setId(userId);
        if (!userService.countByIdAndPassword(applyUser)) {
            return ResponseEntity.badRequest().body("密码不正确！");
        }
        return ResponseEntity.ok().build();
    }
}
