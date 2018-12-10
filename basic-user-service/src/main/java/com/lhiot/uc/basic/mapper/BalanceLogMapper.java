package com.lhiot.uc.basic.mapper;

import com.lhiot.uc.basic.entity.BalanceLog;
import com.lhiot.uc.basic.model.BalanceLogParam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangfeng created in 2018/9/12 12:08
 **/
@Mapper
@Repository
public interface BalanceLogMapper {

    int insert(BalanceLog balanceLog);

    /**
     * 查询鲜果币操作记录列表
     *
     * @param param 参数
     * @return 鲜果币操作记录列表
     */
    List<BalanceLog> findList(BalanceLogParam param);

    /**
     * 查询鲜果币操作记录总数
     *
     * @param param 参数
     * @return 总数
     */
    int findCount(BalanceLogParam param);
}
