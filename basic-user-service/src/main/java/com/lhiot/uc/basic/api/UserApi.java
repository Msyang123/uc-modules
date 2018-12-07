package com.lhiot.uc.basic.api;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.StringUtils;
import com.leon.microx.web.result.Pages;
import com.leon.microx.web.result.Tuple;
import com.leon.microx.web.swagger.ApiHideBodyProperty;
import com.leon.microx.web.swagger.ApiParamType;
import com.lhiot.dc.dictionary.HasEntries;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.entity.SwitchStatus;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.model.*;
import com.lhiot.uc.basic.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangfeng created in 2018/9/7 11:15
 **/
@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserApi {

    private final UserService userService;
    private ApplyUserMapper applyUserMapper;

    public UserApi(UserService userService, ApplyUserMapper applyUserMapper) {
        this.userService = userService;
        this.applyUserMapper = applyUserMapper;
    }

    @ApiOperation(value = "根据用户ID查询用户信息", response = UserDetailResult.class)
    @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataType = "Long", required = true)
    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Long userId) {
        UserDetailResult user = userService.findById(userId);
        if (Objects.equals(user, null)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据用户OpenID查询用户信息", response = UserDetailResult.class)
    @ApiImplicitParam(paramType = "path", name = "openId", value = "用户ID", dataType = "String", required = true)
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
            @ApiImplicitParam(paramType = "query", name = "applicationType", value = "应用类型", dataTypeClass = String.class, required = true)
    })
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity findByPhone(@PathVariable("phoneNumber") String phoneNumber, @Valid @RequestParam("applicationType")@HasEntries(from = "applications") String applicationType) {
        UserDetailResult user = userService.findByPhone(phoneNumber, applicationType);
        if (Objects.equals(user, null)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "根据查询条件查询用户信息", response = UserDetailResult.class, responseContainer = "Set")
    @ApiImplicitParam(paramType = "body", name = "param", dataType = "SearchParam", required = true, value = "查询条件 ")
    @PostMapping("/search")
    public ResponseEntity findUserList(@RequestBody SearchParam param) {
        if (StringUtils.isNotBlank(param.getIds())) {
            return ResponseEntity.ok(userService.findUsersByIds(param.getIds()));
        }
        if (StringUtils.isNotBlank(param.getPhones())) {
            return ResponseEntity.ok(userService.findUsersByPhones(param.getPhones()));
        }
        if (StringUtils.isNotBlank(param.getKeyword())) {
            return ResponseEntity.ok(userService.findByKeyword(param.getKeyword()));
        }
        return ResponseEntity.ok(Tuple.of(new ArrayList<>()));
    }

    @ApiOperation(value = "根据业务用户ID修改用户信息", notes = "根据业务用户ID修改用户信息！")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "传入参数", required = true, dataType = "Long"),
            @ApiImplicitParam(paramType = "body", name = "param", value = "传入参数", required = true, dataType = "ModificationUserParam")
    })
    @PutMapping("/{id}")
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

    @ApiOperation("修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(paramType = "body", name = "param", value = "密码信息", dataTypeClass = PasswordParam.class, required = true)
    })
    @PutMapping("/{id}/password")
    public ResponseEntity updatePassword(@PathVariable("id") Long userId, @RequestParam PasswordParam param) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setPassword(param.getPassword());
        user.setPaymentPassword(param.getPaymentPassword());
        return applyUserMapper.updatePasswordById(user) > 0 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改密码失败！");
    }

    @ApiOperation("修改用户鲜果币支付免密权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "用户ID", dataTypeClass = Long.class, required = true),
            @ApiImplicitParam(paramType = "query", name = "paymentPermissions", value = "免密权限", dataTypeClass = SwitchStatus.class, required = true)
    })
    @PutMapping("/{id}/payment-permissions")
    public ResponseEntity updatePaymentPassword(@PathVariable("id") Long userId, @RequestParam SwitchStatus paymentPermissions) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setPaymentPermissions(paymentPermissions);
        return userService.updatePaymentPermissionsById(user) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改免密权限失败失败！");
    }

    @ApiOperation("根据用户账号和密码查询用户信息")
    @ApiImplicitParam(paramType = "body", name = "param", value = "用户信息", dataType = "PhoneAndPasswordSearchParam", required = true)
    @PostMapping("/phone-and-password/search")
    public ResponseEntity findByPhoneAndPassword(@RequestBody PhoneAndPasswordSearchParam param) {
        UserDetailResult userDetailResult = applyUserMapper.findByPhoneAndPassword(param);
        if (Objects.isNull(userDetailResult)) {
            return ResponseEntity.badRequest().body("用户不存在或密码不正确！");
        }
        return ResponseEntity.ok().body(userDetailResult);
    }

    @ApiOperation("根据业务用户Id修改unionId")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "userId", value = "业务用户Id", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "query", name = "unionId", value = "微信的unionId", dataType = "String", required = true)
    })
    @PutMapping("/{userId}/union-id")
    public ResponseEntity updateUnionId(@PathVariable("userId") Long userId, @RequestParam String unionId) {
        ApplyUser user = new ApplyUser();
        user.setId(userId);
        user.setUnionId(unionId);
        if (!userService.updateUnionIdByUserId(user)) {
            return ResponseEntity.badRequest().body("更新失败！");
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation("后台管理分页查询用户列表")
    @ApiImplicitParam(paramType = ApiParamType.BODY,name = "querySearch",value = "查询入参",dataTypeClass = QuerySearch.class,required = true)
    @ApiHideBodyProperty("startRow")
    @PostMapping("/query/search")
    public ResponseEntity query(@RequestBody QuerySearch querySearch){
        if (Objects.nonNull(querySearch.getRows()) && Objects.nonNull(querySearch.getPage())){
            querySearch.setStartRow((querySearch.getPage()-1)*querySearch.getRows());
        }
       Pages pages =  userService.userQuery(querySearch);
       return ResponseEntity.ok(pages);
    }

    @ApiOperation("解除用户锁定")
    @ApiImplicitParam(paramType = ApiParamType.PATH,name = "userId",value = "业务用户Id",dataType="Long",required = true)
    @PutMapping("/{userId}/unlocked")
    public ResponseEntity unlock(@PathVariable("userId") Long userId){
        int count = applyUserMapper.updateLockStatus(userId);
        if (count ==1 ){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("解锁失败");
    }
}
