package com.lhiot.uc.basic.service;

import com.leon.microx.util.Maps;
import com.leon.microx.util.Retry;
import com.leon.microx.web.result.Tips;
import com.lhiot.uc.basic.entity.SwitchStatus;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhangfeng created in 2018/9/12 10:51
 **/
@Service
@Slf4j
@Transactional
public class BalancePaymentService {
    private BaseUserMapper baseUserMapper;

    public BalancePaymentService(BaseUserMapper baseUserMapper) {
        this.baseUserMapper = baseUserMapper;
    }

    /**
     * 用户扣除鲜果币
     *
     * @param userId         基础用户Id
     * @param operationMoney 需要扣除的金额
     * @param map
     * @return
     */
    public Tips subCurrency(Long balance, Long operationMoney, Long userId, String paymentPassword, Map<String, Object> map) {
        SwitchStatus permissions = SwitchStatus.valueOf((String) map.get("paymentPermissions"));
        /**支付权限开启---->可以进行余额支付
         * 支付权限未开启---->支付密码正确可进行余额支付，支付密码错误返回错误信息
         */
        if (!Objects.equals(SwitchStatus.OPEN, permissions) && !Objects.equals(paymentPassword, map.get("paymentPassword"))) {
            return Tips.warn("支付密码错误！");
        }
        Retry<Boolean> retry = Retry.of(() -> {
            int count = baseUserMapper.updateCurrencyByApplyUserIdForSub(Maps.of("id", userId, "balance", balance, "money", operationMoney));
            if (count <= 0) {
                throw new RuntimeException("减鲜果币失败！");
            }
            return true;
        }).count(3).intervalMs(30L).run();
        if (Objects.nonNull(retry.exception())) {
            return Tips.warn("扣除鲜果币失败！");
        }
        return Tips.info("扣除成功！");
    }
}
