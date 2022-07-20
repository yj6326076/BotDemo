package com.example.simple.danmu.service;

import com.example.simple.danmu.entity.DamnEntity;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

/**
 * @author yj632
 */
public interface DanmuService {
    /**
     * 获取弹幕数据
     * @param start 弹幕开始时间
     * @return 弹幕数据
     */
    List<DamnEntity> getDanmuList(Date start);

    void uploadFileByDate(OutputStream outputStream, Date start);
}
