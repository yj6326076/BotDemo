package com.example.simple.bot.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.simple.bot.entity.SensitiveWordEntity;
import com.example.simple.bot.repository.SensitiveWordRepository;
import com.example.simple.bot.repository.elastic.document.SensitiveWordDocument;
import com.example.simple.bot.repository.elastic.repository.SingleElasticsearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

/**
 * elasticsearch
 * @author yj632
 */
@RestController
@Slf4j
@RequestMapping("/elasticsearch")
@ConditionalOnBean(SensitiveWordDocument.class)
public class ElasticsearchController {
    @Inject
    SingleElasticsearchRepository singleElasticsearchRepository;

    @Inject
    SensitiveWordRepository sensitiveWordRepository;

    @PostMapping("/count")
    public long count() {
        return singleElasticsearchRepository.count();
    }

    @PostMapping("/load")
    public long load() {
        List<SensitiveWordEntity> all = sensitiveWordRepository.findAll();
        List<SensitiveWordDocument> sensitiveWordDocuments = JSONObject.parseArray(JSONObject.toJSONString(all)).toJavaList(SensitiveWordDocument.class);
        singleElasticsearchRepository.saveAll(sensitiveWordDocuments);
        return sensitiveWordDocuments.size();
    }

    @PostMapping("/search")
    public Page<SensitiveWordDocument> search(@RequestBody SensitiveWordDocument search) {
        return singleElasticsearchRepository.findByWord(search.getWord(), Pageable.ofSize(20));
    }
}
