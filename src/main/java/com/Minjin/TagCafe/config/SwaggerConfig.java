package com.Minjin.TagCafe.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TagCafe API")
                        .description("카공러 맞춤형 카페 정보 플랫폼 TagCafe의 API 명세서입니다.")
                        .version("v1.0.0"));
    }
}
