package com.wms.common;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")   // 允许任意域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 加上 OPTIONS
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("*");
    }
}
