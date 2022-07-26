package com.example.simple.bot.service;

import com.example.simple.common.vo.ResultVo;

/**
 * 机器人基础操作接口
 * 负责进行各种基础操作最简单为执行方法
 * 额外添加了基于查询数据的innerWhite方法后续优化数据筛选方法
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


    /**
     * 是否数据白名单
     * @param msg 消息
     * @param t 处理后实体类
     * @return 校验结果
     */
    default boolean isInnerWhite(String msg, T t) {
        return true;
    }
}
