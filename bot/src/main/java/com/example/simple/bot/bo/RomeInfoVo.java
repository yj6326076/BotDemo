package com.example.simple.bot.bo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yj632
 */
@Data
public class RomeInfoVo {
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @JSONField(name = "show_status")
    private String status;
    @JSONField(name = "room_name")
    private String romeName;
    @JSONField(name = "nickname")
    private String upName;
    @JSONField(name = "room_src")
    private String imageUrl;
    @JSONField(name = "show_time")
    private Date startDate;
    @JSONField(name = "end_time")
    private Date endDate;

    public String formatRomeInfo() {
        if ("1".equals(status)) {
            return upName + "正在直播，\n 直播间标题为\"" + romeName + "\"\n" +
                    "开播时间为" + simpleDateFormat.format(startDate);
        }
        return upName + "正在休息，\n 上次直播时，直播间标题为\"" + romeName + "\"\n" +
                "下播时间为" + simpleDateFormat.format(endDate);
    }
}
