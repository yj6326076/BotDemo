package com.example.simple.danmu.repository;

import com.example.simple.danmu.entity.DamnEntity;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 弹幕保存服务
 * @author yj632
 */
@Repository
@ConditionalOnBean(name = "danmuWebSocket")
public interface DamnRepository extends JpaRepository<DamnEntity,Long> {
    /**
     * 查询所有所需时间的记录
     * @param createDate 开始时间
     * @return 记录
     */
    List<DamnEntity> findAllByCreatDateAfter(Date createDate);
}
