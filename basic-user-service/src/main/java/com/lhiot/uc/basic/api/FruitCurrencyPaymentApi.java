package com.lhiot.uc.basic.api;

import com.leon.microx.util.BeanUtils;
import com.lhiot.uc.basic.entity.CurrencyLog;
import com.lhiot.uc.basic.entity.OperationStatus;
import com.lhiot.uc.basic.model.CurrencyOperationParam;
import com.lhiot.uc.basic.service.FruitCurrencyService;
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
public class FruitCurrencyPaymentApi {
    private FruitCurrencyService fruitCurrencyService;

    public FruitCurrencyPaymentApi(FruitCurrencyService fruitCurrencyService) {
        this.fruitCurrencyService = fruitCurrencyService;
    }

    @ApiOperation("用户鲜果币加减")
    @ApiImplicitParam(paramType = "body", name = "param", value = "用户加减鲜果币操作", dataType = "CurrencyOperationParam", dataTypeClass = CurrencyOperationParam.class, required = true)
    @PutMapping("/currency/operation")
    public ResponseEntity userCurrencyOperation(@RequestBody CurrencyOperationParam param) {

        if (Objects.equals(OperationStatus.ADD, param.getOperation())) {
            boolean flag = fruitCurrencyService.addCurrency(param.getBaseUserId(), param.getOperationCurrency());
            if (!flag) {
                return ResponseEntity.badRequest().body("增加鲜果币失败！");
            }
        } else {
            boolean flag = fruitCurrencyService.subCurrency(param.getBaseUserId(), param.getOperationCurrency());
            if (!flag) {
                return ResponseEntity.badRequest().body("扣除鲜果币失败！");
            }
        }
        CurrencyLog currencyLog = new CurrencyLog();
        BeanUtils.of(currencyLog).populate(param);
        currencyLog.setCurrency(param.getOperationCurrency());
        fruitCurrencyService.addCurrencyLog(currencyLog);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("查询用户余额")
    @ApiImplicitParam(paramType = "path", name = "id", value = "基础用户Id", dataType = "Long", required = true)
    @GetMapping("/{id}")
    public ResponseEntity findFruitCurrency(@PathVariable("id") Long baseUserId) {
        Long currency = fruitCurrencyService.findCurrencyById(baseUserId);
        if (Objects.isNull(currency)) {
            return ResponseEntity.badRequest().body("用户不存在！");
        }
        return ResponseEntity.ok(currency);
    }
}
