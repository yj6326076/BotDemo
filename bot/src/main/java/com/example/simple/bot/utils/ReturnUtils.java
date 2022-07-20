package com.example.simple.bot.utils;

import com.example.simple.bot.bo.ResultVo;

/**
 * 构造返回结果工具类
 * @author yj632
 */
public class ReturnUtils {
    public static final Integer SUCCESS = 200;
    public static final Integer ERROR = 406;

    public static <T> ResultVo<T> success(T data, String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setCode(SUCCESS);
        resultVo.setMessage(message);
        resultVo.setData(data);
        return resultVo;
    }

    public static <T> ResultVo<T> error(T data,String message) {
        ResultVo<T> resultVo = new ResultVo<>();
        resultVo.setCode(ERROR);
        resultVo.setMessage(message);
        resultVo.setData(data);
        return resultVo;
    }

    public static <T> ResultVo<T> error(T data) {
        return error(data, null);
    }

    public static <T> ResultVo<T> success(T data) {
        return success(data, null);
    }

    public static boolean isSuccess(ResultVo resultVo) {
        return SUCCESS.equals(resultVo.getCode());
    }
}
