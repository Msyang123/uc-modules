package com.lhiot.uc.basic.mapper;

import lombok.Builder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserExtensionMapper {
    int save(UserExtensionParam param);

    @Builder
    class UserExtensionParam{
        String inviteCode;
        Long userId;
    }
}
