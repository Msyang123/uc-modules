package com.lhiot.uc.basic.api;

import com.leon.microx.support.result.Tips;
import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.basic.entity.BalanceLog;
import com.lhiot.uc.basic.entity.OperationStatus;
import com.lhiot.uc.basic.model.BalanceOperationParam;
import com.lhiot.uc.basic.service.BalancePaymentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @Author zhangfeng created in 2018/9/12 14:40
 **/
@RestController
@Slf4j
@RequestMapping("/users/fruits")
public class BalancePaymentApi {
    private BalancePaymentService balancePaymentService;

    public BalancePaymentApi(BalancePaymentService balancePaymentService) {
        this.balancePaymentService = balancePaymentService;
    }

    @ApiOperation("用户鲜果币加减")
    @ApiImplicitParam(paramType = "body", name = "param", value = "用户加减鲜果币操作", dataType = "CurrencyOperationParam", dataTypeClass = BalanceOperationParam.class, required = true)
    @PutMapping("/balance/operation")
    public ResponseEntity userBalanceOperation(@RequestBody BalanceOperationParam param) {
        Long balance = balancePaymentService.findCurrencyById(param.getBaseUserId());
        if (Objects.isNull(balance)) {
            ResponseEntity.badRequest().body("用户不存在!");
        }
        long money = param.getMoney();
        long baseUserId = param.getBaseUserId();
        if (Objects.equals(OperationStatus.ADD, param.getOperation())) {
            boolean flag = balancePaymentService.addCurrency(baseUserId, money);
            if (!flag) {
                return ResponseEntity.badRequest().body("增加鲜果币失败！");
            }
        } else {
            if (balance < money) {
                return ResponseEntity.badRequest().body("余额不足");
            }
            boolean flag = balancePaymentService.subCurrency(balance, money, baseUserId);
            if (!flag) {
                return ResponseEntity.badRequest().body("扣除鲜果币失败！");
            }
        }
        BalanceLog balanceLog = new BalanceLog();
        BeanUtils.of(balanceLog).populate(param);
        balanceLog.setMoney(money);
        balancePaymentService.addCurrencyLog(balanceLog);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("查询用户余额")
    @ApiImplicitParam(paramType = "path", name = "id", value = "基础用户Id", dataType = "Long", required = true)
    @GetMapping("/{id}")
    public ResponseEntity findFruitCurrency(@PathVariable("id") Long baseUserId) {
        Long balance = balancePaymentService.findCurrencyById(baseUserId);
        if (Objects.isNull(balance)) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        return ResponseEntity.ok(balance);
    }
}
