package com.example.simple.bot.repository;

import com.example.simple.bot.entity.SensitiveWordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 存取敏感词
 * @author yj632
 */
public interface SensitiveWordRepository extends JpaRepository<SensitiveWordEntity,Long> {
}
