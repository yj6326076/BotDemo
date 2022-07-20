package com.example.simple.bot.service;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * 处理聊天记录
 * @author yj632
 */
public interface ContractService {
    /**
     * 处理群消息
     * @param groupMessageEvent 群消息事件
     */
    public void doGroupEvent(GroupMessageEvent groupMessageEvent) throws IOException, ParseException;
    /**
     * 处理好友消息
     * @param friendMessageEvent 好友消息事件
     */
    public void doFriendEvent(FriendMessageEvent friendMessageEvent) throws IOException, ParseException;
}
