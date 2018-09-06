package com.lhiot.uc.basic.service;

import com.lhiot.uc.basic.model.PhoneRegisterParam;
import com.lhiot.uc.basic.mapper.BaseUserMapper;
import com.lhiot.uc.basic.entity.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseUserService {

    private final BaseUserMapper baseUserMapper;

    public BaseUserService(BaseUserMapper baseUserMapper) {
        this.baseUserMapper = baseUserMapper;
    }

    /**
     * 添加基础用户信息
     * @param param
     * @return
     */
    public int save(Long id,PhoneRegisterParam param){
      return   baseUserMapper.save(new BaseUser().copy(id,param));
    }
}
