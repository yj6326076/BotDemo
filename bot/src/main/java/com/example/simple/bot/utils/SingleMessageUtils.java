package com.example.simple.bot.utils;

import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.entity.NicknameEntity;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息处理类
 * @author yj632
 */
public class SingleMessageUtils {
    private static final Pattern BIND_PATTERN = Pattern.compile("((bd)|(绑定)|(bind))[ ,，]([^ ,，]*)[ ,，](\\d+)");
    private static final Pattern VIEW_PATTERN = Pattern.compile("((查)|(探)|(view))[ ,，]([^ ,，]*)");
    private static final Pattern CHECK_PATTERN = Pattern.compile("((danmu)|(弹幕))[ ,，]([^ ,，]*)");
    public static ResultVo<NicknameEntity> parseToNickname(String message) {
        Matcher matcher = BIND_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return ReturnUtils.error(null,"格式错误");
        }
        NicknameEntity nicknameVo = new NicknameEntity();
        nicknameVo.setNickname(matcher.group(5));
        nicknameVo.setRomeId(NumberUtils.toLong(matcher.group(6)));
        return ReturnUtils.success(nicknameVo,"解析绑定格式正确");
    }

    public static String parseNameOrRome(String message) {
        Matcher matcher = VIEW_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(5);
    }

    public static String parseCheck(String message) {
        Matcher matcher = CHECK_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(4);
    }
}
