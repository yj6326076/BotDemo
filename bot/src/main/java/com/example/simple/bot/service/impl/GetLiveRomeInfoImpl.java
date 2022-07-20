package com.example.simple.bot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.service.GetLiveRomeInfo;
import com.example.simple.bot.utils.ReturnUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查询直播房间服务
 * @author yj632
 */
@Service
public class GetLiveRomeInfoImpl implements GetLiveRomeInfo {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResultVo<RomeInfoVo> getLiveRomeInfo(Long romeId) throws IOException, ParseException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://www.douyu.com/betard/" + romeId);
        CloseableHttpResponse execute = client.execute(httpGet);
        String resultString = EntityUtils.toString(execute.getEntity());
        if (StringUtils.isEmpty(resultString) || !resultString.startsWith("{")) {
            return ReturnUtils.success(null,"查询房间错误，请确认房间号是否正确");
        }
        JSONObject response = JSONObject.parseObject(resultString);
        if (null == response) {
            return ReturnUtils.success(null,"查询房间错误，请确认房间号是否正确");
        }
        JSONObject room = response.getJSONObject("room");
        if (null == room) {
            return ReturnUtils.success(null,"查询房间错误，请确认房间号是否正确");
        }
        String showStatus = room.getString("show_status");
        RomeInfoVo romeInfoVo = room.toJavaObject(RomeInfoVo.class);
        if ("1".equals(showStatus)) {
            String romeInfo = room.getString("nickname") + "正在直播，\n 直播间标题为\"" + room.getString("room_name") + "\"\n" +
                    "开播时间为" + simpleDateFormat.format(new Date(room.getLong("show_time") * 1000));
            return ReturnUtils.success(romeInfoVo,romeInfo);
        }
        String romeInfo = room.getString("nickname") + "正在休息，\n 上次直播时，直播间标题为\"" + room.getString("room_name") + "\"\n" +
                "下播时间为" + simpleDateFormat.format(new Date(room.getLong("end_time") * 1000));
        return ReturnUtils.success(romeInfoVo,romeInfo);
    }
}
