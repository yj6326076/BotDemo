package com.example.simple.bot.repository;

import com.example.simple.bot.entity.LiveStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * @author yj632
 */
public interface LiveStatusRepository extends JpaRepository<LiveStatusEntity, Long> {
    /**
     * 查询今天的直播状态
     * @param liveDate 直播日期
     * @return 查询结果
     */
    LiveStatusEntity findFirstByLiveDate(Date liveDate);
}
