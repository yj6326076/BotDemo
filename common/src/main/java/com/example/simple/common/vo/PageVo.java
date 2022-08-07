package com.example.simple.common.vo;

import lombok.Data;

/**
 * 分页参数
 * @author yj632
 */
@Data
public class PageVo<T> {
    private int size;

    private int page;

    private T data;
}
