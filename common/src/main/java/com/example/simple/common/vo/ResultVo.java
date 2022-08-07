package com.example.simple.common.vo;

import lombok.Data;

/**
 * 结果bo
 * @author yj632
 */
@Data
public class ResultVo<T> {
    private Integer code;
    private String message;
    private T data;
}
