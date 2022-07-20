package com.example.simple.bot.service.impl;

import com.example.simple.bot.bo.CurrentContractVo;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.service.NickNameService;
import com.example.simple.bot.utils.ImageUploadUtils;
import com.example.simple.bot.utils.LocalHostContextUtils;
import com.example.simple.bot.utils.ReturnUtils;
import com.example.simple.bot.utils.SingleMessageUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;

/**
 * 保存别名服务
 * @author yj632
 */
@Slf4j
@Service
public class NickNameSaveServiceImpl implements BaseRunnerService<NicknameEntity> {
    @Inject
    NickNameService nickNameService;

    /**
     * 通用格式化方法
     *
     * @param msg 待格式化消息
     * @return 格式化结果
     */
    @Override
    public NicknameEntity parseMsg(String msg) {
        ResultVo<NicknameEntity> nicknameEntityResultVo = SingleMessageUtils.parseToNickname(msg);
        if (!ReturnUtils.isSuccess(nicknameEntityResultVo)) {
            log.error("bind error message:{} result:{}",msg,nicknameEntityResultVo.getMessage());
            return null;
        }
        return nicknameEntityResultVo.getData();
    }

    /**
     * 处理群消息操作
     *
     * @param msg 消息内容
     * @param entity 处理后消息
     * @return 处理结果
     */
    @Override
    public ResultVo<NicknameEntity> doGroupOperation(String msg,NicknameEntity entity) {
        if (null == entity) {
            return ReturnUtils.error( null, "cannot parse msg");
        }
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        GroupMessageEvent groupMessageEvent = currentContract.getGroupMessageEvent();
        if (null == groupMessageEvent) {
            String errorMsg = "cannot find default group";
            log.error(errorMsg);
            return ReturnUtils.error( null, errorMsg);
        }
        long senderId = groupMessageEvent.getSender().getId();
        Contact group = groupMessageEvent.getGroup();
        return saveNickName(senderId, group, entity);
    }

    /**
     * 处理好友消息操作
     *
     * @param msg 消息内容
     * @param entity 处理后消息
     * @return 处理结果
     */
    @Override
    public ResultVo<NicknameEntity> doFriendOperation(String msg,NicknameEntity entity) {
        if (null == entity) {
            return ReturnUtils.error( null, "cannot parse msg");
        }
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        FriendMessageEvent friendMessageEvent = currentContract.getFriendMessageEvent();
        if (null == friendMessageEvent) {
            String errorMsg = "cannot find default group";
            log.error(errorMsg);
            return ReturnUtils.error( null, errorMsg);
        }
        long senderId = friendMessageEvent.getSender().getId();
        Contact friend = friendMessageEvent.getFriend();
        return saveNickName(senderId, friend, entity);
    }

    private ResultVo<NicknameEntity> saveNickName(long senderId, Contact contact, NicknameEntity entity) {
        entity.setCreateId(senderId);
        entity.setLastUpdateId(senderId);

        try {
            ResultVo<RomeInfoVo> nicknameEntityResultVo = nickNameService.saveNickname(entity);
            if (ReturnUtils.isSuccess(nicknameEntityResultVo)) {
                Image image = ImageUploadUtils.upload(nicknameEntityResultVo.getData(), contact);
                contact.sendMessage(new PlainText(nicknameEntityResultVo.getMessage()).plus(image));
                return ReturnUtils.success(entity);
            }
        } catch (IOException | ParseException e) {
            log.error("save nick name error", e);
        }
        return ReturnUtils.error( null, "save nick name error");
    }
}
