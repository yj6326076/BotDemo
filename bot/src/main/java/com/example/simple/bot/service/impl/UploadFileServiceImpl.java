package com.example.simple.bot.service.impl;

import com.example.simple.bot.service.UploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author yj632
 */
@Slf4j
@Service
@ConditionalOnMissingBean(UploadFileService.class)
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public ByteArrayOutputStream uploadFile(Date date) {
        return null;
    }
}
