package com.example.simple.bot.service;

import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

/**
 * 获取直播信息
 * @author yj632
 */
public interface GetLiveRomeInfo {
    /**
     * 查询直播房间信息
     * @param romeId 直播房间id
     * @return 房间信息
     */
    public ResultVo<RomeInfoVo> getLiveRomeInfo(Long romeId) throws IOException, ParseException;
}
