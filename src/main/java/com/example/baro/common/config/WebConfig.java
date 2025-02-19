package com.example.baro.common.config;

import com.example.baro.common.resolver.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final LoginUserArgumentResolver loginUserArgumentResolver;

	public WebConfig(LoginUserArgumentResolver loginUserArgumentResolver) {
		this.loginUserArgumentResolver = loginUserArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginUserArgumentResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 엔드포인트에 CORS 적용
				.allowedOrigins("http://localhost:5173", "https://barobaro.netlify.app") // 프론트엔드 주소
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
				.allowedHeaders("*") // 모든 헤더 허용
				.allowCredentials(true); // 인증 정보 포함 허용
	}
}
