package com.lhiot.uc.basic.service;

import com.leon.microx.util.ImmutableMap;
import com.leon.microx.util.Retry;
import com.lhiot.uc.basic.entity.BaseUser;
import com.lhiot.uc.basic.entity.CurrencyLog;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.mapper.CurrencyLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author zhangfeng created in 2018/9/12 10:51
 **/
@Service
@Slf4j
@Transactional
public class FruitCurrencyService {
    private BaseUserMapper baseUserMapper;
    private CurrencyLogMapper currencyLogMapper;

    public FruitCurrencyService(BaseUserMapper baseUserMapper, CurrencyLogMapper currencyLogMapper) {
        this.baseUserMapper = baseUserMapper;
        this.currencyLogMapper = currencyLogMapper;
    }

    /**
     * 用户扣除鲜果币
     *
     * @param baseUserId        基础用户Id
     * @param operationCurrency 需要扣除的鲜果币
     * @return
     */
    public boolean subCurrency(Long baseUserId, Long operationCurrency) {
        Retry<Boolean> retry = Retry.of(() -> {
            Long currency = baseUserMapper.findCurrencyById(baseUserId);
            if (currency < operationCurrency) {
                return false;
            }
            int count = baseUserMapper.updateCurrencyByIdForSub(ImmutableMap.of("id", baseUserId, "currency", currency, "operationCurrency", operationCurrency));
            if (count <= 0) {
                throw new RuntimeException("减鲜果币失败！");
            }
            return true;
        }).count(3).intervalMs(30L).run();
        if (Objects.nonNull(retry.exception())) {
            return false;
        }
        return retry.result();
    }

    /**
     * 增加鲜果币
     *
     * @param baseUserId
     * @param operationCurrency
     * @return
     */
    public boolean addCurrency(Long baseUserId, Long operationCurrency) {
        BaseUser user = new BaseUser();
        user.setId(baseUserId);
        user.setCurrency(operationCurrency);
        return baseUserMapper.updateCurrencyByIdForAdd(user) > 0;
    }

    public boolean addCurrencyLog(CurrencyLog currencyLog) {
        return currencyLogMapper.insert(currencyLog) > 0;
    }

    public Long findCurrencyById(Long baseUserId){
       return baseUserMapper.findCurrencyById(baseUserId);
    }
}
