package com.example.simple.danmu.config;

import com.example.simple.danmu.entity.DamnEntity;
import com.example.simple.danmu.property.DanmuProperties;
import com.example.simple.danmu.repository.DamnRepository;
import com.example.simple.danmu.utils.DouyuDamUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.inject.Inject;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;

/**
 * @author yj632
 */
@Slf4j
@Configuration
public class SimpleWebSocketListener implements Listener {
    @Inject
    private DamnRepository damnRepository;

    @Override
    public void onOpen(WebSocket webSocket) {
        webSocket.request(1);
        log.info("webSocket opening");
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        webSocket.request(1);
        log.info("webSocket onText");
        return null;
    }

    @Override
    public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
        webSocket.request(1);
        DamnEntity transform = DouyuDamUtils.transform(data);
        if (transform == null) {
            return null;
        }
        log.info("webSocket onBinary , msg: {},name:{}", transform.getMessage(),transform.getName());
        damnRepository.save(transform);
        return null;
    }

    @Override
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        log.info("webSocket onPing");
        return null;
    }

    @Override
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        log.info("webSocket onPong");
        return null;
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        log.info("webSocket onClose statusCode:{},reason:{}",statusCode, reason);
        return null;
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
        log.error("webSocket onError", error);
    }
}
