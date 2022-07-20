package com.example.simple.bot.bo;

import lombok.Data;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * 当前聊天信息
 * @author yj632
 */
@Data
public class CurrentContractVo {
    /**
     * 是否群消息
     */
    private boolean isGroup;

    /**
     * 群消息事件
     */
    private GroupMessageEvent groupMessageEvent;

    /**
     * 好友消息事件
     */
    private FriendMessageEvent friendMessageEvent;

    /**
     * 是否管理员
     */
    private boolean isAdmin;

    /**
     * 是否白名单模式
     */
    private boolean isWhite;
}
