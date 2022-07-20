package com.example.simple.danmu.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 导出excel
 * @author yj632
 */
@Data
public class DamnVo {
    @ExcelProperty("弹幕信息")
    private String message;

    @ExcelProperty("用户名")
    private String name;

    @ExcelProperty("用户id")
    private String senderId;

    @ExcelProperty("粉丝牌名称")
    private String title;

    @ExcelProperty("发送时间")
    private Date creatDate;
}
