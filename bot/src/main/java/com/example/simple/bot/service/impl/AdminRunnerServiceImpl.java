package com.example.simple.bot.service.impl;

import com.example.simple.bot.annotation.BotConsumer;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.bot.repository.NicknameRepository;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.utils.SingleMessageUtils;
import com.example.simple.common.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;

/**
 * 管理员操作服务
 * @author yj632
 */
@Slf4j
@Service
@BotConsumer(regex = "config", isAdmin = true)
public class AdminRunnerServiceImpl implements BaseRunnerService<NicknameEntity> {

    @Inject
    NicknameRepository nicknameRepository;

    /**
     * 通用格式化方法
     *
     * @param msg 待格式化消息
     * @return 格式化结果
     */
    @Override
    public NicknameEntity parseMsg(String msg) {
        String nameOrRome = SingleMessageUtils.parseNameOrRome(msg);
        NicknameEntity nicknameVo = new NicknameEntity();
        nicknameVo.setNickname(nameOrRome);
        Optional<NicknameEntity> one = nicknameRepository.findOne(Example.of(nicknameVo));
        return one.orElse(null);
    }

    /**
     * 处理群消息操作
     *
     * @param msg    消息内容
     * @param entity 处理后入参
     * @return 处理结果
     */
    @Override
    public ResultVo<NicknameEntity> doGroupOperation(String msg, NicknameEntity entity) {
        return null;
    }

    /**
     * 处理好友消息操作
     *
     * @param msg    消息内容
     * @param entity 处理后入参
     * @return 处理结果
     */
    @Override
    public ResultVo<NicknameEntity> doFriendOperation(String msg, NicknameEntity entity) {
        return null;
    }
}
