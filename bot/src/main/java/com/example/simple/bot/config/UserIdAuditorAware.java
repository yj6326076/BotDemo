package com.example.simple.bot.config;

import com.example.simple.bot.bo.CurrentContractVo;
import com.example.simple.bot.utils.LocalHostContextUtils;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * 更新用户id服务
 *
 * @author yj632
 */
@Configuration
public class UserIdAuditorAware implements AuditorAware<Long> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @NotNull
    @Override
    public Optional<Long> getCurrentAuditor() {
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        if (currentContract.isGroup()) {
            GroupMessageEvent groupMessageEvent = currentContract.getGroupMessageEvent();
            Long id = groupMessageEvent.getSender().getId();
            return Optional.of(id);
        }
        FriendMessageEvent friendMessageEvent = currentContract.getFriendMessageEvent();
        if (null == friendMessageEvent) {
            return Optional.empty();
        }
        return Optional.of(friendMessageEvent.getUser().getId());
    }
}
