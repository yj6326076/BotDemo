package com.example.simple.bot.repository;

import com.example.simple.bot.entity.NicknameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author yj632
 */
public interface NicknameRepository extends JpaRepository<NicknameEntity,Long> {
    /**
     * 查询别名
     * @param nickname 别名
     * @return 查询结果
     */
    List<NicknameEntity> findAllByNickname(String nickname);

    /**
     * 查询房间号记录
     * @param romeId 房间号
     * @return 查询结果
     */
    NicknameEntity findFirstByRomeId(Long romeId);
}
