package com.example.simple.bot.utils;

import com.example.simple.bot.bo.CurrentContractVo;

/**
 * 线程自定义工具类
 * @author yj632
 */
public class LocalHostContextUtils {
    static ThreadLocal<CurrentContractVo> contractVoThreadLocal = new ThreadLocal<>();

    /**
     * 工具初始化线程参数
     * @param currentContract 消息参数
     */
    public static void setCurrentContract(CurrentContractVo currentContract) {
        contractVoThreadLocal.set(currentContract);
    }

    /**
     * 工具获取线程参数
     * @return 线程参数
     */
    public static CurrentContractVo getCurrentContract() {
        return contractVoThreadLocal.get();
    }


    /**
     * 工具清楚线程参数，避免内存泄露
     */
    public static void removeCurrentContract() {
        contractVoThreadLocal.remove();
    }
}
