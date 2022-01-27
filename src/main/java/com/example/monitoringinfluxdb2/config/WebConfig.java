package com.example.monitoringinfluxdb2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // controller 경로를 /api/* 로 만들거기 떄문에 그 경로만 허용한다
    registry
        .addMapping("/**")
        .allowedOrigins("http://192.168.120.8:3000");
  }
}
