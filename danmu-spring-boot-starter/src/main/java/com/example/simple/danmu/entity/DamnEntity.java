package com.example.simple.danmu.entity;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 弹幕实体
 * @author yj632
 */
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@ConditionalOnBean(name = "danmuWebSocket")
@Data
public class DamnEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String message;

    private String name;

    private String senderId;

    private String title;

    @CreatedDate
    private Date creatDate;

    @LastModifiedDate
    private Date lastUpdateDate;

    private String lastUpdateId;
}
