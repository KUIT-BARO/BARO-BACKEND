package com.example.baro.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.context.annotation.Bean;

public class CookieConfig {
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None");  // Cross-Site 요청에서도 쿠키 사용 가능
        serializer.setUseSecureCookie(true); // HTTPS 환경에서만 쿠키 전송 (필수)
        return serializer;
    }
}
