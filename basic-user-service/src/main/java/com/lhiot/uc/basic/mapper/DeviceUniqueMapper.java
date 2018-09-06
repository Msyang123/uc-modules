package com.lhiot.uc.basic.mapper;


import com.lhiot.uc.basic.model.DeviceUniqueResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeviceUniqueMapper {

    DeviceUniqueResult findByUkey(String ukey);
}
