package com.online.edu.gateway.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    static final String ORIGINS[] = new String[] { "GET", "POST", "PUT", "DELETE" };
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //映射路径
                .allowedOrigins("*") // 原始域
                .allowCredentials(true) //是否携带cookie
                .allowedMethods(ORIGINS) // 放行那些请求
                .allowedHeaders("*") //放行哪些头部信息
                .maxAge(3600);
    }
}
