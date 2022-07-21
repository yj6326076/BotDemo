package com.example.simple.bot.config;

import com.alibaba.fastjson.JSONObject;
import com.example.simple.bot.bo.CurrentContractVo;
import com.example.simple.bot.bo.RegexConfigVo;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.common.BotSymbol;
import com.example.simple.bot.entity.UserRightEntity;
import com.example.simple.bot.property.BotProperties;
import com.example.simple.bot.repository.UserRightRepository;
import com.example.simple.bot.service.BaseRunnerService;
import com.example.simple.bot.service.ContractService;
import com.example.simple.bot.utils.LocalHostContextUtils;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.inject.Inject;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 机器人配置
 *
 * @author yj632
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(BotProperties.class)
@ComponentScan(basePackages = {"com.example.simple.bot"})
@EnableJpaRepositories(basePackages = {"com.example.simple.bot",})
@EntityScan(basePackages = {"com.example.simple.bot",})
public class BotConfig {
    @Inject
    BotProperties botProperties;

    @Inject
    UserRightRepository userRightRepository;

    @Inject
    Map<String, BaseRunnerService> baseRunnerServiceMap;

    @Bean("bot")
    public Bot getBot() {
        Bot bot = BotFactory.INSTANCE.newBot(botProperties.getLoginId(), botProperties.getLoginPassword());

        EventChannel<BotEvent> eventChannel = bot.getEventChannel();
        eventChannel.subscribeAlways(GroupMessageEvent.class, event -> {
            CurrentContractVo currentContractVo = initContract(event);
            Disposable subscribe = Flowable.just(event).subscribe(item -> {
                LocalHostContextUtils.setCurrentContract(currentContractVo);
                String msg = item.getMessage().contentToString();
                BaseRunnerService baseRunner = getBaseRunner(msg);
                if (null == baseRunner) {
                    return;
                }
                Object entity = baseRunner.parseMsg(msg);
                ResultVo resultVo = baseRunner.doGroupOperation(msg, entity);
                log.info("result is {}", JSONObject.toJSONString(resultVo));
                LocalHostContextUtils.removeCurrentContract();
            });
            subscribe.dispose();
        });
        eventChannel.subscribeAlways(FriendMessageEvent.class, event -> {
            CurrentContractVo currentContractVo = initContract(event);
            Disposable subscribe = Flowable.just(event).subscribe(item -> {
                LocalHostContextUtils.setCurrentContract(currentContractVo);
                String msg = item.getMessage().contentToString();
                BaseRunnerService baseRunner = getBaseRunner(msg);
                if (null == baseRunner) {
                    return;
                }
                Object entity = baseRunner.parseMsg(msg);
                boolean innerWhite = baseRunner.isInnerWhite(msg, entity);
                if (innerWhite) {
                    ResultVo resultVo = baseRunner.doFriendOperation(msg, entity);
                    log.info("result is {}", JSONObject.toJSONString(resultVo));
                }
                LocalHostContextUtils.removeCurrentContract();
            });
            subscribe.dispose();
        });

        bot.login();
        if (null != botProperties.getNoticeFriendId() && 0 != botProperties.getNoticeFriendId()) {
            Friend friend = bot.getFriend(botProperties.getNoticeFriendId());
            if (friend != null) {
                friend.sendMessage("start success");
            }
        }

        if (null != botProperties.getNoticeGroupId() && 0 != botProperties.getNoticeGroupId()) {
            Group group = bot.getGroup(botProperties.getNoticeGroupId());
            if (group != null) {
                group.sendMessage("start success");
            }
        }
        return bot;
    }

    private CurrentContractVo initContract(GroupMessageEvent groupMessageEvent) {
        long id = groupMessageEvent.getGroup().getId();
        long senderId = groupMessageEvent.getSender().getId();
        UserRightEntity group = userRightRepository.findFirstByUserIdAndType(id, "group");
        UserRightEntity user = userRightRepository.findFirstByUserIdAndType(senderId, "user");
        boolean isAdmin = (null != group && BotSymbol.ADMIN.equals(group.getRoleType())) || (null != user && BotSymbol.ADMIN.equals(user.getRoleType()));
        boolean isWhite = (null != group && BotSymbol.WHITE.equals(group.getRoleType())) || (null != user && BotSymbol.WHITE.equals(user.getRoleType()));
        CurrentContractVo currentContractVo = new CurrentContractVo();
        currentContractVo.setGroupMessageEvent(groupMessageEvent);
        currentContractVo.setGroup(true);
        currentContractVo.setAdmin(isAdmin);
        currentContractVo.setWhite(isWhite);
        return currentContractVo;
    }

    private CurrentContractVo initContract(FriendMessageEvent friendMessageEvent) {
        long senderId = friendMessageEvent.getSender().getId();
        UserRightEntity user = userRightRepository.findFirstByUserIdAndType(senderId, "user");
        boolean isAdmin = (null != user && BotSymbol.ADMIN.equals(user.getRoleType()));
        boolean isWhite = (null != user && BotSymbol.WHITE.equals(user.getRoleType()));
        CurrentContractVo currentContractVo = new CurrentContractVo();
        currentContractVo.setFriendMessageEvent(friendMessageEvent);
        currentContractVo.setGroup(false);
        currentContractVo.setAdmin(isAdmin);
        currentContractVo.setWhite(isWhite);
        return currentContractVo;
    }

    private BaseRunnerService getBaseRunner(String msg) {
        for (RegexConfigVo regexConfigVo:botProperties.getConfig()) {
            String regex = regexConfigVo.getRegex();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(msg);
            if (matcher.matches()) {
                // 白名单群、用户只使用白名单功能
                if (LocalHostContextUtils.getCurrentContract().isWhite() && !regexConfigVo.getWhite()) {
                    return null;
                }

                // 管理员功能只允许管理员用户和群使用
                if (regexConfigVo.getAdmin() && LocalHostContextUtils.getCurrentContract().isAdmin()) {
                    return null;
                }
                return baseRunnerServiceMap.get(regexConfigVo.getTarget());
            }
        }
        return null;
    }
}
