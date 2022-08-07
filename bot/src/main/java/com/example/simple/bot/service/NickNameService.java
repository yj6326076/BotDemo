package com.example.simple.bot.service;

import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.common.vo.ResultVo;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * 别名服务
 * @author yj632
 */
public interface NickNameService {
    /**
     * 保存别名
     * @param nicknameVo 保存别名参数
     * @return 保存结果
     */
    ResultVo<RomeInfoVo> saveNickname(NicknameEntity nicknameVo) throws IOException, ParseException;
}
