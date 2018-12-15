package com.lhiot.uc.basic.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author zhangfeng create in 9:39 2018/12/7
 */
@Data
public class QuerySearch {
    private Long userId;

    private String phone;

    private String nickname;

    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAtStart;
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAtEnd;

    private Integer rows;

    private Integer page;
    private Integer startRow;

    private String applicationType;
}
