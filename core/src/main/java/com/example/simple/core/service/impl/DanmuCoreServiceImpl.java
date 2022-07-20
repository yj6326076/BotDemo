package com.example.simple.core.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.example.simple.danmu.entity.DamnEntity;
import com.example.simple.danmu.repository.DamnRepository;
import com.example.simple.danmu.service.DanmuService;
import com.example.simple.danmu.vo.DamnVo;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@Service
public class DanmuCoreServiceImpl implements DanmuService {

    @Inject
    private DamnRepository damnRepository;

    /**
     * 获取弹幕数据
     *
     * @param start 弹幕开始时间
     * @return 弹幕数据
     */
    @Override
    public List<DamnEntity> getDanmuList(Date start) {
        return damnRepository.findAllByCreatDateAfter(start);
    }

    /**
     * 上传文件
     * @param outputStream 上传流
     * @param start 开始时间
     */
    @Override
    public void uploadFileByDate(OutputStream outputStream, Date start) {
        List<DamnEntity> allByCreatDateAfter = damnRepository.findAllByCreatDateAfter(start);
        List<DamnVo> damnVos = JSONObject.parseArray(JSONObject.toJSONString(allByCreatDateAfter), DamnVo.class);
        EasyExcel.write(outputStream, DamnVo.class).sheet("弹幕").doWrite(damnVos);
    }
}
