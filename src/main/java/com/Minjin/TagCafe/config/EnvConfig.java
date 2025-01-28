package com.Minjin.TagCafe.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {
    private static final Dotenv dotenv = Dotenv.load();

    @Bean
    public Dotenv dotenv() {
        return dotenv;
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
