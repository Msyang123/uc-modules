package com.lhiot.uc.basic.api;

import com.leon.microx.util.StringUtils;
import com.lhiot.uc.basic.entity.ApplyUser;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.model.UserBindingPhoneParam;
import com.lhiot.uc.basic.model.UserBindingWeChatParam;
import com.lhiot.uc.basic.model.UserDetailResult;
import com.lhiot.uc.basic.service.UserBindingService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author zhangfeng created in 2018/9/10 18:43
 **/
@RestController
@Slf4j
public class UserBindingApi {

    private UserBindingService userBindingService;
    private ApplyUserMapper applyUserMapper;

    public UserBindingApi(UserBindingService userBindingService, ApplyUserMapper applyUserMapper) {
        this.userBindingService = userBindingService;
        this.applyUserMapper = applyUserMapper;
    }

    @ApiOperation("用户绑定手机号码---在业务上规避一个手机号存在多个账号")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "id",value = "用户Id",dataType = "Long",required = true),
            @ApiImplicitParam(paramType = "body", name = "param", value = "手机号，业务用户Id", dataType = "UserBindingPhoneParam", required = true)
    })
    @PutMapping("/users/{id}/binding-phone")
    public ResponseEntity userBindingPhone(@PathVariable("id") Long userId,@RequestBody UserBindingPhoneParam param) {

        UserDetailResult userDetailResult = applyUserMapper.findById(userId);
        if (Objects.isNull(userDetailResult)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        if (StringUtils.isNotBlank(userDetailResult.getPhone())) {
            return ResponseEntity.badRequest().body("该账号已绑定手机 ！");
        }
        ApplyUser applyUser = new ApplyUser();
        applyUser.setApplicationType(param.getApplicationType());
        applyUser.setPhone(param.getPhone());
        boolean isBinding = userBindingService.isBindingWeChat(applyUser);
        if (isBinding) {
            return ResponseEntity.badRequest().body("该手机号已被其它账号绑定！");
        }

        userDetailResult = userBindingService.bindingPhone(userId,param);
        return ResponseEntity.ok(userDetailResult);
    }

    @ApiOperation("用户绑定微信")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path",name = "id",value = "用户Id",dataType = "Long",required = true),
            @ApiImplicitParam(paramType = "body", name = "param", value = "微信信息", dataType = "UserBindingWeChatParam", dataTypeClass = UserBindingWeChatParam.class, required = true)
    })
    @PutMapping("/users/{id}/binding-we-chat")
    public ResponseEntity userBindingWeChat(@PathVariable("id")Long userId,@RequestBody UserBindingWeChatParam param) {

        UserDetailResult userDetailResult = applyUserMapper.findById(userId);
        if (Objects.isNull(userDetailResult)) {
            return ResponseEntity.badRequest().body("该用户不存在！");
        }
        if (StringUtils.isNotBlank(userDetailResult.getOpenId())) {
            return ResponseEntity.badRequest().body("该账号已绑定微信！");
        }
        boolean isBinding = userBindingService.isBindingPhone(param.getOpenId());
        if (isBinding) {
            return ResponseEntity.badRequest().body("该微信已被其它账号绑定！");
        }
        boolean flag = userBindingService.bindingWeChat(userId,param);
        return flag ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("绑定微信失败！");
    }


    @ApiOperation("微信解除绑定")
    @ApiImplicitParam(paramType = "path", name = "id", value = "业务用户ID", dataType = "Long", required = true)
    @PutMapping("/unbinding/{id}")
    public ResponseEntity removingBindWeChat(@PathVariable("id") Long userId) {
        boolean flag = userBindingService.removingWeChatBinding(userId);
        return flag ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("解绑失败！");
    }
}
