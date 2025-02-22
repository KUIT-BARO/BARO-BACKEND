package com.example.baro.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "BARO API 명세서",
                description = "BARO API 명세서",
                version = "1"))
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi v1Api() {
        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("BARO API v1")  // 그룹 이름을 설정한다.
                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
                .build();
    }
}
