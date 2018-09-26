package com.lhiot.uc.basic.api;

import com.leon.microx.support.result.Multiple;
import com.leon.microx.support.swagger.ApiHideBodyProperty;
import com.lhiot.uc.basic.model.UserAddress;
import com.lhiot.uc.basic.service.UserAddressService;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/users")
public class UserAddressApi {

    private UserAddressService userAddressService;

    public UserAddressApi(UserAddressService userAddressService) {
        this.userAddressService = userAddressService;
    }

    @ApiOperation("添加用户收货地址")
    @PostMapping("/address")
    @ApiHideBodyProperty("id")
    public ResponseEntity createUserAddress(@RequestBody UserAddress userAddress){
       return userAddressService.addUserAddress(userAddress) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("添加失败");
    }

    @ApiOperation("修改用户收货地址")
    @PutMapping("/address")
    @ApiHideBodyProperty({"baseUserId"})
    public ResponseEntity updateUserAddress(@RequestBody UserAddress userAddress){
        return userAddressService.updateAddress(userAddress) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().body("修改失败");
    }

    @ApiOperation("删除用户收货地址")
    @ApiImplicitParam(paramType ="path",name = "ids",value = "收货地址ID集合以逗号隔开",dataType = "String",required = true)
    @DeleteMapping("/address/{ids}")
    public ResponseEntity updateUserAddress(@PathVariable("ids") String ids){
        List<String> idList = Arrays.asList(ids.split(","));
        userAddressService.deleteByIds(idList);
        return  ResponseEntity.ok().build();
    }

    @ApiOperation("查询单个地址详细信息")
    @ApiImplicitParam(paramType = "path",name = "id",value = "地址Id",dataType = "Long",required = true)
    @GetMapping("/address/{id}")
    public ResponseEntity findAddressById(@PathVariable("id") Long id){
       return ResponseEntity.ok().body(userAddressService.findById(id)) ;
    }

    @ApiOperation("根据基础用户查询收货地址列表")
    @ApiImplicitParam(paramType = "path",name = "baseUserId",value = "基础用户Id",dataType = "Long",required = true)
    @GetMapping("/address/user-id/{baseUserId}")
    public ResponseEntity findListByUserId(@PathVariable("baseUserId") Long baseUserId){
        List<UserAddress> addresses = userAddressService.findListByUserId(baseUserId);
        if(CollectionUtils.isEmpty(addresses)){
            return ResponseEntity.ok().body(Multiple.of(new ArrayList<>()));
        }
        return ResponseEntity.ok().body(addresses);
    }
}
