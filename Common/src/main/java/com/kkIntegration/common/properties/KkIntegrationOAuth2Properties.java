package com.kkIntegration.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description:
 * author: ckk
 * create: 2019-12-18 10:55
 */
@Configuration
@ConfigurationProperties(prefix = "kkincegration.oauth2")
public class KkIntegrationOAuth2Properties {

    // token 存活时间 , 默认 12 小时  60 * 60 * 12
    private String accessTokenSeconds = "60 * 60 * 12";

    // refreshToken 存活时间 , 默认 30 天  60 * 60 * 24 * 30;
    private String refreshTokenSeconds = "60 * 60 * 24 * 30";

    // bearerType 自定义
    private String bearerType = "Bearer";

    //  Client ID
    private String clientId = "admin";

    // Client Secret
    private String clientSecret = "{noop}admin";

    public int getAccessTokenSeconds() {
        int time = CalculateTime(accessTokenSeconds);
        return time == 0 ? 60 * 60 * 12 : time;
    }


    public int getRefreshTokenSeconds() {
        int time = CalculateTime(refreshTokenSeconds);
        return time == 0 ? 60 * 60 * 24 * 30 : time;
    }

    // 计算时间
    private int CalculateTime(String second) {
        if (second == null) {
            return 0;
        }
        // * 切分
        String[] seconds = second.split("\\*");
        int time = 0;
        for (String s : seconds) {
            if (s.trim().isEmpty()) {
                continue;
            }
            Integer s1 = Integer.valueOf(s.trim());
            if (time != 0) {
                time *= s1;
            } else {
                time = s1;
            }
        }
        return time;
    }


    public void setAccessTokenSeconds(String accessTokenSeconds) {
        this.accessTokenSeconds = accessTokenSeconds;
    }

    public void setRefreshTokenSeconds(String refreshTokenSeconds) {
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    public String getBearerType() {
        return bearerType;
    }

    public void setBearerType(String bearerType) {
        this.bearerType = bearerType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
