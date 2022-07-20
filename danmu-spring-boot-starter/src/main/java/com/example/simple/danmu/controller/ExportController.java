package com.example.simple.danmu.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.example.simple.danmu.entity.DamnEntity;
import com.example.simple.danmu.repository.DamnRepository;
import com.example.simple.danmu.vo.DamnVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 导出文件controller
 * @author yj632
 */
@Slf4j
@Controller
@ConditionalOnBean(name = "danmuWebSocket")
public class ExportController {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Inject
    DamnRepository damnRepository;

    @GetMapping("/export/danm")
    public void exportDanm(@RequestParam(value = "startTime",required = false) String startTime, HttpServletResponse httpServletResponse) {
        Date date = new Date();
        if (null != startTime) {
            try {
                date = DATE_FORMAT.parse(startTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<DamnEntity> allByCreatDateAfter = damnRepository.findAllByCreatDateAfter(date);
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=danm.xlsx");
        try (ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream()) {
            List<DamnVo> damnVos = JSONObject.parseArray(JSONObject.toJSONString(allByCreatDateAfter), DamnVo.class);
            EasyExcel.write(servletOutputStream, DamnVo.class).sheet("弹幕").doWrite(damnVos);
        } catch (Exception e) {
            log.error("export error", e);
        }
    }
}
