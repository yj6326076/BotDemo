package com.example.simple.common.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author yj632
 */
@Data
public class QueryVo {
    private String name;
    private Date beginDate;
    private Date endDate;
}
