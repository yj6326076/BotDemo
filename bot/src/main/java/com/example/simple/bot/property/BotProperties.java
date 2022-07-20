package com.example.simple.bot.property;

import com.example.simple.bot.bo.RegexConfigVo;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author yj632
 */
@Data
@ConfigurationProperties("bot")
public class BotProperties {
    private Long loginId;
    private String loginPassword;
    private Long noticeFriendId;
    private Long noticeGroupId;
    private List<RegexConfigVo> config;
}
