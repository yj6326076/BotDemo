package com.example.simple.bot.repository.elastic.document;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.annotation.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * 敏感词实体类
 * @author yj632
 */
@Document(indexName = "sensitive-word1")
@Data
@ConditionalOnProperty(prefix = "bot", name = "enableElastic", havingValue = "true")
public class SensitiveWordDocument {
    @Id
    private Long id;

    @Field(searchAnalyzer = "ik_smart",analyzer = "ik_max_word",type = FieldType.Text)
    private String word;
}
