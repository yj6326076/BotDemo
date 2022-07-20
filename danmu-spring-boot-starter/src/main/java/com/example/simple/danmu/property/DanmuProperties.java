package com.example.simple.danmu.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

/**
 * @author yj632
 */
@ConfigurationProperties("danmu")
public class DanmuProperties {
    private Boolean isEnable = true;

    private URI url;

    private Long romeId;

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public Long getRomeId() {
        return romeId;
    }

    public void setRomeId(Long romeId) {
        this.romeId = romeId;
    }
}
