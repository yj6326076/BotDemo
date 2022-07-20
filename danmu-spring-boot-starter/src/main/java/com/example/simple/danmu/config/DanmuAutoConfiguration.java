package com.example.simple.danmu.config;

import com.example.simple.danmu.property.DanmuProperties;
import com.example.simple.danmu.utils.DouyuDamUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 简单的websocket
 * @author yj632
 */
@Configuration
@EnableConfigurationProperties(DanmuProperties.class)
@AutoConfigureAfter(SimpleWebSocketListener.class)
@EnableScheduling
@ConditionalOnProperty(prefix = "danmu", name = "isEnable", havingValue = "true")
@ComponentScan(basePackages = {"com.example.simple.danmu"})
@EnableJpaRepositories(basePackages = {"com.example.simple.danmu",})
@EntityScan(basePackages = {"com.example.simple.danmu",})
@Slf4j
public class DanmuAutoConfiguration {
    @Autowired
    private DanmuProperties danmuProperties;

    @Autowired
    private SimpleWebSocketListener simpleWebSocketListener;

    @Bean(name = "danmuWebSocket")
    public WebSocket webSocket() throws Exception {
        CompletableFuture<WebSocket> webSocketCompletableFuture = HttpClient.newBuilder()
                .build().newWebSocketBuilder().buildAsync(danmuProperties.getUrl(), simpleWebSocketListener);
        WebSocket webSocket = webSocketCompletableFuture.get(1000, TimeUnit.SECONDS);
        byte[] bytes = DouyuDamUtils.loginRome(danmuProperties.getRomeId());
        log.info("login byte is {}",Hex.encodeHex(bytes));
        byte[] join = DouyuDamUtils.join(danmuProperties.getRomeId());
        log.info("join byte is {}",Hex.encodeHex(join));
        webSocket.sendBinary(ByteBuffer.wrap(bytes),false);
        webSocket.sendBinary(ByteBuffer.wrap(join),false);
        return webSocket;
    }
}
