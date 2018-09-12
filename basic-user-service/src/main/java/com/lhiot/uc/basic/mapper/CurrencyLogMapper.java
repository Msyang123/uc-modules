package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.CurrencyLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author zhangfeng created in 2018/9/12 12:08
 **/
@Mapper
@Repository
public interface CurrencyLogMapper {

    int insert(CurrencyLog currencyLog);
}
