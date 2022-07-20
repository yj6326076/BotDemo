package com.example.simple.bot.service.impl;

import com.example.simple.bot.bo.CurrentContractVo;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.service.UploadFileService;
import com.example.simple.bot.utils.LocalHostContextUtils;
import com.example.simple.bot.utils.ReturnUtils;
import com.example.simple.bot.utils.SingleMessageUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 弹幕服务
 * @author yj632
 * @since 2022-07-19
 */
@Slf4j
@Service
public class DanmuRunnerServiceImpl implements BaseRunnerService<String> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    UploadFileService uploadFileService;

    /**
     * 处理群消息操作
     *
     * @param msg 消息内容
     * @param afterMsg 处理后消息
     * @return 处理结果
     */
    @Override
    public ResultVo<String> doGroupOperation(String msg, String afterMsg) {
        String check = SingleMessageUtils.parseCheck(msg);
        try {
            Date parse = DATE_FORMAT.parse(check + " 12:00:00");
            CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
            GroupMessageEvent groupMessageEvent = currentContract.getGroupMessageEvent();
            Group group = groupMessageEvent.getGroup();
            if (null == group) {
                String errorMsg = "cannot find default group";
                log.error(errorMsg);
                return ReturnUtils.error(errorMsg);
            }
            ByteArrayOutputStream byteArrayOutputStream = uploadFileService.uploadFile(parse);
            try (InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray())) {
                ExternalResource externalResource = ExternalResource.create(inputStream).toAutoCloseable();
                group.getFiles().uploadNewFile("/danmu" + check + ".xlsx", externalResource);
            }
        } catch (ParseException | IOException e) {
            log.error("parse date error", e);
        }
        return null;
    }

    /**
     * 处理好友消息操作
     *
     * @param msg 消息内容
     * @param afterMsg 处理后消息
     * @return 处理结果
     */
    @Override
    public ResultVo<String> doFriendOperation(String msg, String afterMsg) {
        CurrentContractVo currentContract = LocalHostContextUtils.getCurrentContract();
        FriendMessageEvent friendMessageEvent = currentContract.getFriendMessageEvent();
        if (null == friendMessageEvent) {
            log.error("DanmuRunnerServiceImpl===>doFriendOperation get no friend message error");
            return ReturnUtils.error("get no friend message error");
        }
        friendMessageEvent.getFriend().sendMessage("暂未支持好友弹幕查询，请耐心等待后续功能开放");
        return ReturnUtils.success("暂未支持好友弹幕查询，请耐心等待后续功能开放");
    }
}
