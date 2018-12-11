package com.lhiot.uc.basic.model;

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

    private Date createAtStart;

    private Date createAtEnd;

    private Integer rows;

    private Integer page;
    private Integer startRow;

    private String applicationType;
}
