package com.example.simple.bot.repository;

import com.example.simple.bot.entity.UserRightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author yj632
 */
public interface UserRightRepository extends JpaRepository<UserRightEntity,Long> {
    /**
     * 查询用户权限
     * @param userId 用户id
     * @param type 类型
     * @return 查询结果
     */
    public UserRightEntity findFirstByUserIdAndType(Long userId, String type);
}