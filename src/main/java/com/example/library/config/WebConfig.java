package com.example.library.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
     @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // atau "*"
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOrigins("https://3f3bbbeac50575b47ddb6f11d13c8efa.serveo.net/")
                .allowCredentials(false);
    }
}
