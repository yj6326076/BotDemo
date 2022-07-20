package com.example.simple.bot.repository.elastic.repository;

import com.example.simple.bot.repository.elastic.document.SensitiveWordDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * elasticSearch
 * @author yj632
 */
@Repository
@ConditionalOnBean(SensitiveWordDocument.class)
public interface SingleElasticsearchRepository extends ElasticsearchRepository<SensitiveWordDocument,Long> {
    /**
     * 查询关键词
     * @param word 关键词
     * @param pageable 分页
     * @return 查询结果
     */
    @Query("{\"match\": {\"word\": {\"query\": \"?0\"}}}")
    Page<SensitiveWordDocument> findByWord(String word, Pageable pageable);
}
