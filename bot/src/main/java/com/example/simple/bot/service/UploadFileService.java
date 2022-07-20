package com.example.simple.bot.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * 上传文档
 * @author yj632
 */
public interface UploadFileService {

    public ByteArrayOutputStream uploadFile(Date date);
}
