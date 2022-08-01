package com.example.simple.bot.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author yj632
 */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class LiveStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String status;

    @Temporal(TemporalType.DATE)
    private Date liveDate;

    @CreatedBy
    private Long createdBy;

    @CreatedDate
    private Date creatDate;

    @LastModifiedBy
    private Long lastUpdateBy;

    @LastModifiedDate
    private Date lastUpdateDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        LiveStatusEntity that = (LiveStatusEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
