package com.example.simple.bot.service.impl;

import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.entity.LiveStatusEntity;
import com.example.simple.bot.repository.LiveStatusRepository;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.utils.LocalHostContextUtils;
import com.example.simple.bot.utils.ReturnUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;

/**
 * @author yj632
 */
@Slf4j
@Service
public class LiveStatusServiceImpl implements BaseRunnerService<LiveStatusEntity> {

    @Inject
    LiveStatusRepository liveStatusRepository;

    /**
     * 通用格式化方法
     *
     * @param msg 待格式化消息
     * @return 格式化结果
     */
    @Override
    public LiveStatusEntity parseMsg(String msg) {
        return BaseRunnerService.super.parseMsg(msg);
    }

    /**
     * 处理群消息操作
     *
     * @param msg              消息内容
     * @param liveStatusEntity 处理后入参
     * @return 处理结果
     */
    @Override
    public ResultVo<LiveStatusEntity> doGroupOperation(String msg, LiveStatusEntity liveStatusEntity) {
        LiveStatusEntity firstByLiveDate = liveStatusRepository.findFirstByLiveDate(new Date());
        if (null == firstByLiveDate || "N".equalsIgnoreCase(firstByLiveDate.getStatus())) {
            GroupMessageEvent groupMessageEvent = LocalHostContextUtils.getCurrentContract().getGroupMessageEvent();
            groupMessageEvent.getGroup().sendMessage("");
        }
        return ReturnUtils.success(liveStatusEntity);
    }

    /**
     * 处理好友消息操作
     *
     * @param msg              消息内容
     * @param liveStatusEntity 处理后入参
     * @return 处理结果
     */
    @Override
    public ResultVo<LiveStatusEntity> doFriendOperation(String msg, LiveStatusEntity liveStatusEntity) {
        liveStatusRepository.save(liveStatusEntity);
        return ReturnUtils.success(liveStatusEntity);
    }
}
