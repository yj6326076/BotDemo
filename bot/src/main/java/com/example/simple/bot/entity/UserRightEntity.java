package com.example.simple.bot.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author yj632
 */
@Entity
@Table
@EntityListeners(AuditingEntityListener.class)
@Data
public class UserRightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long userId;

    private String type;

    private String roleType;

    private Long createId;

    @CreatedDate
    private Date creatDate;

    @LastModifiedDate
    private Date lastUpdateDate;

    private Long lastUpdateId;
}
