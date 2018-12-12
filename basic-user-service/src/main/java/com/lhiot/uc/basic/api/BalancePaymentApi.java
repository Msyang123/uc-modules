package com.lhiot.uc.basic.api;

import com.leon.microx.util.BeanUtils;
import com.leon.microx.util.Maps;
import com.leon.microx.web.result.Pages;
import com.leon.microx.web.result.Tips;
import com.leon.microx.web.swagger.ApiParamType;
import com.lhiot.uc.basic.entity.BalanceLog;
import com.lhiot.uc.basic.entity.OperationStatus;
import com.lhiot.uc.basic.mapper.ApplyUserMapper;
import com.lhiot.uc.basic.mapper.BalanceLogMapper;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.model.BalanceLogParam;
import com.lhiot.uc.basic.model.BalanceOperationParam;
import com.lhiot.uc.basic.service.BalancePaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhangfeng created in 2018/9/12 14:40
 **/
@RestController
@Slf4j
public class BalancePaymentApi {
    private BalancePaymentService balancePaymentService;
    private BaseUserMapper baseUserMapper;
    private BalanceLogMapper balanceLogMapper;
    private ApplyUserMapper applyUserMapper;

    public BalancePaymentApi(BalancePaymentService balancePaymentService, BaseUserMapper baseUserMapper, BalanceLogMapper balanceLogMapper, ApplyUserMapper applyUserMapper) {
        this.balancePaymentService = balancePaymentService;
        this.baseUserMapper = baseUserMapper;
        this.balanceLogMapper = balanceLogMapper;
        this.applyUserMapper = applyUserMapper;
    }

    @ApiOperation("用户鲜果币加减")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "业务用户Id", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "body", name = "param", value = "用户加减鲜果币操作", dataType = "BalanceOperationParam", dataTypeClass = BalanceOperationParam.class, required = true)
    })
    @PutMapping("/users/{id}/balance")
    public ResponseEntity userBalanceOperation(@PathVariable("id") Long userId, @RequestBody BalanceOperationParam param) {
        //返回balance,paymentPassword,paymentPermissions
        Map<String, Object> map = baseUserMapper.findPaymentPermissionsByApplyUserId(userId);
        if (CollectionUtils.isEmpty(map)) {
            ResponseEntity.badRequest().body("用户不存在!");
        }
        long money = param.getMoney();
        if (Objects.equals(OperationStatus.ADD, param.getOperation())) {
            boolean flag = baseUserMapper.updateCurrencyByApplyUserIdForAdd(Maps.of("id", userId, "money", money)) > 0;
            if (!flag) {
                return ResponseEntity.badRequest().body("增加鲜果币失败！");
            }
        } else {
            Long balance = (Long) map.get("balance");
            if (Objects.isNull(balance) || balance < money) {
                return ResponseEntity.badRequest().body("余额不足");
            }
            Tips tips = balancePaymentService.subCurrency(balance, money, userId, param.getPaymentPassword(), map);
            if (Objects.equals(HttpStatus.BAD_REQUEST.toString(), tips.getCode())) {
                return ResponseEntity.badRequest().body(tips.getMessage());
            }
        }
        Long baseUserId = applyUserMapper.findBaseUserId(userId);
        BalanceLog balanceLog = new BalanceLog();
        BeanUtils.of(balanceLog).populate(param);
        balanceLog.setMoney(money);
        balanceLog.setBaseUserId(baseUserId);
        balanceLogMapper.insert(balanceLog);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("查询用户余额")
    @ApiImplicitParam(paramType = "path", name = "id", value = "业务用户Id", dataType = "Long", required = true)
    @GetMapping("/users/{id}/balance")
    public ResponseEntity findFruitCurrency(@PathVariable("id") Long userId) {
        Long balance = baseUserMapper.findCurrencyByApplyUserId(userId);
        if (Objects.isNull(balance)) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        return ResponseEntity.ok(balance);
    }

    @ApiOperation("根据基础用户Id添加鲜果币(用户仓库转水果专用)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "id", value = "基础用户Id", dataType = "Long", required = true),
            @ApiImplicitParam(paramType = "path", name = "money", value = "添加鲜果币金额", dataType = "Long", required = true)
    })
    @PutMapping("/users/{id}/balance/{money}")
    public ResponseEntity balanceAdd(@PathVariable("id") Long baseUserId, @PathVariable("money") Long money) {
        baseUserMapper.updateBalanceByIdForAdd(Maps.of("id", baseUserId, "money", money));
        BalanceLog balanceLog = new BalanceLog();
        balanceLog.setBaseUserId(baseUserId);
        balanceLog.setMoney(money);
        balanceLog.setOperation(OperationStatus.ADD);
        balanceLog.setMemo("仓库转换鲜果币");
        balanceLogMapper.insert(balanceLog);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "根据条件分页获取鲜果币操作记录列表", response = BalanceLog.class, responseContainer = "Set")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = ApiParamType.BODY, name = "param", value = "查询条件", dataType = "BalanceLogParam")
    })
    @PostMapping("/balance-logs/pages")
    public ResponseEntity search(@RequestBody BalanceLogParam param) {
        log.debug("获取鲜果币操作记录列表\t param:{}", param);
        List<BalanceLog> list = balanceLogMapper.findList(param);
        int total = param.getPageFlag() ? balanceLogMapper.findCount(param) : list.size();
        return ResponseEntity.ok(Pages.of(total, list));
    }

}
