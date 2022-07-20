package com.example.simple.bot.service.impl;

import com.example.simple.bot.bo.ResultVo;
import com.example.simple.bot.bo.RomeInfoVo;
import com.example.simple.bot.entity.NicknameEntity;
import com.example.simple.bot.repository.NicknameRepository;
import com.example.simple.bot.service.GetLiveRomeInfo;
import com.example.simple.bot.service.NickNameService;
import com.example.simple.bot.utils.ReturnUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * 别名服务实现类
 * @author yj632
 */
@Slf4j
@Service
public class NickNameServiceImpl implements NickNameService {

    @Inject
    GetLiveRomeInfo getLiveRomeInfo;

    @Inject
    NicknameRepository nicknameRepository;

    /**
     * 保存别名
     *
     * @param nicknameVo 保存别名参数
     * @return 保存结果
     */
    @Override
    public ResultVo<RomeInfoVo> saveNickname(NicknameEntity nicknameVo) throws IOException, ParseException {
        log.info("nickname sensitive check success");
        ResultVo<RomeInfoVo> liveRomeInfo = getLiveRomeInfo.getLiveRomeInfo(nicknameVo.getRomeId());
        if (ReturnUtils.ERROR.equals(liveRomeInfo.getCode())) {
            log.info("nickname rome check fail");
            return ReturnUtils.error(null,liveRomeInfo.getMessage());
        }
        log.info("nickname rome check success");
        List<NicknameEntity> allByNickname = nicknameRepository.findAllByNickname(nicknameVo.getNickname());
        if (CollectionUtils.isEmpty(allByNickname)) {
            nicknameRepository.save(nicknameVo);
            return ReturnUtils.success(liveRomeInfo.getData(),"save nickname success");
        }
        log.info("nickname sensitive save success");
        return ReturnUtils.error(null,"别名已存在");
    }
}
