package com.example.simple.bot.service;

import com.example.simple.bot.bo.ResultVo;

/**
 * 基础操作接口
 *
 * @author yj632
 */
public interface BaseRunnerService<T> {

    /**
     * 通用格式化方法
     * @param msg 待格式化消息
     * @return 格式化结果
     */
    default T parseMsg(String msg) {
        return null;
    }

    /**
     * 处理群消息操作
     * @param msg 消息内容
     * @param t 处理后入参
     * @return 处理结果
     */
    public ResultVo<T> doGroupOperation(String msg, T t);

    /**
     * 处理好友消息操作
     * @param msg 消息内容
     * @param t 处理后入参
     * @return 处理结果
     */
    public ResultVo<T> doFriendOperation(String msg, T t);
}
