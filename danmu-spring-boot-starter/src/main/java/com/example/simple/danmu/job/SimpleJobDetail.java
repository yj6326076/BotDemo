package com.example.simple.danmu.job;

import com.example.simple.danmu.config.DanmuAutoConfiguration;
import com.example.simple.danmu.property.DanmuProperties;
import com.example.simple.danmu.utils.DouyuDamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;

import javax.inject.Inject;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;

/**
 * @author yj632
 */
@Configuration
@Slf4j
public class SimpleJobDetail {
    @Inject
    private DanmuProperties danmuProperties;

    @Inject
    private DanmuAutoConfiguration danmuAutoConfiguration;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    WebSocket webSocket;

    @Scheduled(cron = "0/30 * * * * ?")
    public void executeInternal() throws JobExecutionException {
        log.info("start send heart beat");
        webSocket.sendBinary(ByteBuffer.wrap(DouyuDamUtils.hartBeat()),false);
    }
}
