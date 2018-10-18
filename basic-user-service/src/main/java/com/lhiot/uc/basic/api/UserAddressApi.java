package com.lhiot.uc.basic.api;

import com.leon.microx.web.result.Multiple;
import com.leon.microx.web.swagger.ApiHideBodyProperty;
import com.lhiot.uc.basic.mapper.UserAddressMapper;
import com.lhiot.uc.basic.model.UserAddress;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangfeng created in 2018/9/17 15:59
 **/
@RestController
@Slf4j
public class UserAddressApi {

    private UserAddressMapper userAddressMapper;

    public UserAddressApi(UserAddressMapper userAddressMapper) {
        this.userAddressMapper = userAddressMapper;
    }

    @ApiOperation("添加用户收货地址")
    @PostMapping("/user-addresses")
    @ApiHideBodyProperty("id")
    public ResponseEntity createUserAddress(@RequestBody UserAddress userAddress) {
        return userAddressMapper.insert(userAddress) > 0 ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("添加失败");
    }

    @ApiOperation("修改用户收货地址")
    @PutMapping("/user-addresses/{id}")
    @ApiHideBodyProperty({"baseUserId"})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "地址Id", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "body", name = "userAddress", value = "用户收货地址", dataType = "UserAddress", required = true)
    })
    public ResponseEntity updateUserAddress(@PathVariable("id") Long id, @RequestBody UserAddress userAddress) {
        userAddress.setId(id);
        userAddressMapper.updateAddress(userAddress);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除用户收货地址")
    @ApiImplicitParam(paramType = "path", name = "ids", value = "收货地址ID集合以逗号隔开", dataType = "String", required = true)
    @DeleteMapping("/user-addresses/{ids}")
    public ResponseEntity updateUserAddress(@PathVariable("ids") String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        userAddressMapper.deleteByIds(idList);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("查询单个地址详细信息")
    @ApiImplicitParam(paramType = "path", name = "id", value = "地址Id", dataType = "Long", required = true)
    @GetMapping("/user-addresses/{id}")
    public ResponseEntity findAddressById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(userAddressMapper.findById(id));
    }

    @ApiOperation("根据基础用户查询收货地址列表")
    @ApiImplicitParam(paramType = "path", name = "userId", value = "基础用户Id", dataType = "Long", required = true)
    @GetMapping("/users/{id}/addresses")
    public ResponseEntity findListByUserId(@PathVariable("id") Long userId) {
        List<UserAddress> addresses = userAddressMapper.findListByUserId(userId);
        if (CollectionUtils.isEmpty(addresses)) {
            return ResponseEntity.ok().body(Multiple.of(new ArrayList<>()));
        }
        return ResponseEntity.ok().body(addresses);
    }
}
