package com.example.simple.bot.service.impl;

import com.example.simple.bot.bo.CurrentContractVo;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.common.BotSymbol;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.bot.entity.UserRightEntity;
import com.example.simple.bot.repository.NicknameRepository;
import com.example.simple.bot.repository.UserRightRepository;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.service.GetLiveRomeInfo;
import com.example.simple.bot.utils.ImageUploadUtils;
import com.example.simple.bot.utils.LocalHostContextUtils;
import com.example.simple.bot.utils.ReturnUtils;
import com.example.simple.bot.utils.SingleMessageUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.hc.core5.http.ParseException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

/**
 * 查询直播信息服务
 * @author yj632
 */
@Slf4j
@Service
public class QueryLiveInfoServiceImpl implements BaseRunnerService<NicknameEntity> {

    @Inject
    NicknameRepository nicknameRepository;

    @Inject
    GetLiveRomeInfo getLiveRomeInfo;

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
        if (null == entity) {
            return ReturnUtils.error(null);
        }
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        GroupMessageEvent groupMessageEvent = currentContract.getGroupMessageEvent();
        try {
            return queryLiveInfo(entity, groupMessageEvent.getGroup(), currentContract.isWhite());
        } catch (IOException | ParseException e) {
            log.error("query rome info error, msg:{}", msg, e);
        }
        return ReturnUtils.error(entity);
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
        if (null == entity) {
            return ReturnUtils.error(null);
        }
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        FriendMessageEvent friendMessageEvent = currentContract.getFriendMessageEvent();
        try {
            return queryLiveInfo(entity, friendMessageEvent.getFriend(), currentContract.isWhite());
        } catch (IOException | ParseException e) {
            log.error("query rome info error, msg:{}", msg, e);
        }
        return ReturnUtils.error(entity);
    }

    private ResultVo<NicknameEntity> queryLiveInfo(NicknameEntity entity, Contact contact, boolean isWhite) throws IOException, ParseException {
        if (isWhite && !BotSymbol.WHITE.equals(entity.getRomeType())) {
            return ReturnUtils.success(entity);
        }
        Long romeId = entity.getRomeId();
        ResultVo<RomeInfoVo> liveRomeInfo = getLiveRomeInfo.getLiveRomeInfo(romeId);
        if (ReturnUtils.isSuccess(liveRomeInfo)) {
            Image image = ImageUploadUtils.upload(liveRomeInfo.getData(), contact);
            contact.sendMessage(new PlainText(liveRomeInfo.getMessage()).plus(image));
            return ReturnUtils.success(entity);
        }
        log.error("query rome info error result:{}",liveRomeInfo.getMessage());
        return ReturnUtils.error(entity, "query rome info error");
    }
}
