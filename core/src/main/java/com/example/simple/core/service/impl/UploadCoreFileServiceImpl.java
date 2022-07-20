package com.example.simple.core.service.impl;

import com.example.simple.bot.service.UploadFileService;
import com.example.simple.danmu.service.DanmuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * 上传
 * @author yj632
 */
@Service
@Slf4j
public class UploadCoreFileServiceImpl implements UploadFileService {
    @Inject
    DanmuService danmuService;

    @Override
    public ByteArrayOutputStream uploadFile(Date date) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        danmuService.uploadFileByDate(byteArrayOutputStream, date);
        return byteArrayOutputStream;
    }
}
