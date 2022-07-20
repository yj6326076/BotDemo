package com.example.simple.bot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.bot.entity.UserRightEntity;
import com.example.simple.bot.property.BotProperties;
import com.example.simple.bot.repository.NicknameRepository;
import com.example.simple.bot.repository.UserRightRepository;
import com.example.simple.bot.service.ContractService;
import com.example.simple.bot.service.GetLiveRomeInfo;
import com.example.simple.bot.service.NickNameService;
import com.example.simple.bot.service.UploadFileService;
import com.example.simple.bot.utils.ImageUploadUtils;
import com.example.simple.bot.utils.ReturnUtils;
import com.example.simple.bot.utils.SingleMessageUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hc.core5.http.ParseException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 处理聊天记录
 * @author yj632
 */
@Service
@Slf4j
public class BindContractServiceImpl implements ContractService {
    private static final String WHITE = "white";
    private static final String ADMIN = "admin";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    NickNameService nickNameService;

    @Inject
    NicknameRepository nicknameRepository;

    @Inject
    UserRightRepository userRightRepository;

    @Inject
    GetLiveRomeInfo getLiveRomeInfo;

    @Inject
    BotProperties botProperties;

    @Inject
    UploadFileService uploadFileService;

    /**
     * 处理群消息
     *
     * @param groupMessageEvent 群消息事件
     */
    @Override
    public void doGroupEvent(GroupMessageEvent groupMessageEvent) throws IOException, ParseException {
        long id = groupMessageEvent.getGroup().getId();
        long senderId = groupMessageEvent.getSender().getId();
        String name = groupMessageEvent.getSenderName();
        UserRightEntity group = userRightRepository.findFirstByUserIdAndType(id, "group");
        UserRightEntity user = userRightRepository.findFirstByUserIdAndType(senderId, "user");
        boolean isAdmin = (null != group && ADMIN.equals(group.getRoleType())) || (null != user && ADMIN.equals(user.getRoleType()));
        boolean isWhite = (null != group && WHITE.equals(group.getRoleType())) || (null != user && WHITE.equals(user.getRoleType()));
        for (SingleMessage singleMessage : groupMessageEvent.getMessage()) {
            String message = singleMessage.contentToString().trim();
            log.info("{} send message {}", name, message);
            Group group1 = groupMessageEvent.getSubject();
            doSomeThing(senderId, isAdmin, isWhite,message, group1,groupMessageEvent,null);
        }
    }

    /**
     * 处理好友消息
     *
     * @param friendMessageEvent 好友消息事件
     */
    @Override
    public void doFriendEvent(FriendMessageEvent friendMessageEvent) throws IOException, ParseException {
        long senderId = friendMessageEvent.getSender().getId();
        String name = friendMessageEvent.getSenderName();
        UserRightEntity user = userRightRepository.findFirstByUserIdAndType(senderId, "user");
        boolean isAdmin = null != user && ADMIN.equals(user.getRoleType());
        boolean isWhite = null != user && WHITE.equals(user.getRoleType());
        for (SingleMessage singleMessage : friendMessageEvent.getMessage()) {
            String message = singleMessage.contentToString().trim();
            Friend friend = friendMessageEvent.getFriend();
            log.info("{} send message {}", name, message);
            doSomeThing(senderId, isAdmin, isWhite ,message, friend, null, friendMessageEvent);
        }
    }

    private void doSomeThing(long senderId, boolean isAdmin,boolean isWhite, String message, Contact contact,GroupMessageEvent groupMessageEvent,FriendMessageEvent friendMessageEvent) throws IOException, ParseException {
        if (StringUtils.startsWithAny(message, "bind", "绑定", "bd")) {
            if (isWhite) {
                return;
            }
            ResultVo<NicknameEntity> nicknameVoResultVo = SingleMessageUtils.parseToNickname(message);
            if (!ReturnUtils.isSuccess(nicknameVoResultVo)) {
                log.error("bind error message:{} result:{}",message,nicknameVoResultVo.getMessage());
                return;
            }
            NicknameEntity nicknameVo = nicknameVoResultVo.getData();
            nicknameVo.setCreateId(senderId);
            nicknameVo.setLastUpdateId(senderId);
            if (isAdmin) {
                nicknameRepository.save(nicknameVo);
                log.info("admin bd success!");
                return;
            }
            ResultVo<RomeInfoVo> nicknameEntityResultVo = nickNameService.saveNickname(nicknameVo);
            if (ReturnUtils.isSuccess(nicknameEntityResultVo)) {
                Image image = ImageUploadUtils.upload(nicknameEntityResultVo.getData(), contact);
                contact.sendMessage(new PlainText(nicknameEntityResultVo.getMessage()).plus(image));
                return;
            }
            log.error("bind error message:{} result:{}",message,nicknameEntityResultVo.getMessage());
        } else if (StringUtils.startsWithAny(message, "查", "探", "view")) {
            String nameOrRome = SingleMessageUtils.parseNameOrRome(message);
            NicknameEntity nicknameVo = new NicknameEntity();
            nicknameVo.setNickname(nameOrRome);
            Optional<NicknameEntity> one = nicknameRepository.findOne(Example.of(nicknameVo));
            if (one.isEmpty() && NumberUtils.isCreatable(nameOrRome)) {
                if (isAdmin) {
                    ResultVo<RomeInfoVo> liveRomeInfo = getLiveRomeInfo.getLiveRomeInfo(NumberUtils.toLong(nameOrRome));
                    if (ReturnUtils.isSuccess(liveRomeInfo)) {
                        Image image = ImageUploadUtils.upload(liveRomeInfo.getData(), contact);
                        contact.sendMessage(new PlainText(liveRomeInfo.getMessage()).plus(image));
                        return;
                    }
                }
                one = Optional.ofNullable(nicknameRepository.findFirstByRomeId(NumberUtils.toLong(nameOrRome)));
            }
            if (one.isPresent()) {
                NicknameEntity nicknameEntity = one.get();
                if (isWhite && !WHITE.equals(nicknameEntity.getRomeType())) {
                    return;
                }
                Long romeId = nicknameEntity.getRomeId();
                ResultVo<RomeInfoVo> liveRomeInfo = getLiveRomeInfo.getLiveRomeInfo(romeId);
                if (ReturnUtils.isSuccess(liveRomeInfo)) {
                    Image image = ImageUploadUtils.upload(liveRomeInfo.getData(), contact);
                    contact.sendMessage(new PlainText(liveRomeInfo.getMessage()).plus(image));
                    return;
                }
                log.error("bind error message:{} result:{}",message,liveRomeInfo.getMessage());
            }
        } else if (StringUtils.equalsAny(message,"info","说明")) {
            if (isWhite) {
                return;
            }
            String desc = "目前支持的功能：\n"
                    + "【\"bind\\绑定\\bd\",别名,房间名】 进行房间绑定\n"
                    + "例：\"bd,弯弯,10729343\"\n"
                    + "【\"查\\探\\view\",别名\\房间名】 进行房间状态查询\n"
                    + "例：\"查,弯弯\"\n";
            contact.sendMessage(desc);
        } else if (StringUtils.equalsAny(message,"storge","存货")) {
            if (isWhite) {
                return;
            }
            List<NicknameEntity> all = nicknameRepository.findAll();
            Map<Long, Set<String>> collect = all.stream().collect(Collectors.groupingBy(NicknameEntity::getRomeId, Collectors.mapping(NicknameEntity::getNickname, Collectors.toSet())));
            String s = JSONObject.toJSONString(collect, SerializerFeature.PrettyFormat);
            contact.sendMessage(s);
        } else if (StringUtils.startsWithAny(message,"danmu","弹幕")) {
            if (isWhite) {
                return;
            }
            String check = SingleMessageUtils.parseCheck(message);
            try {
                Date parse = DATE_FORMAT.parse(check + " 12:00:00");
                Bot bot = contact.getBot();
                Group group = bot.getGroup(botProperties.getNoticeGroupId());
                if (null == group) {
                    log.error("cannot find default group");
                    return;
                }
                ByteArrayOutputStream byteArrayOutputStream = uploadFileService.uploadFile(parse);
                try (InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                    ExternalResource externalResource = ExternalResource.create(inputStream).toAutoCloseable();
                    group.getFiles().uploadNewFile("/danmu" + check + ".xlsx", externalResource);
                }
            } catch (java.text.ParseException e) {
                log.error("parse date error", e);
            }
        }
    }
}
