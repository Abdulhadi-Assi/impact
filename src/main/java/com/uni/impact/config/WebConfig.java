package com.uni.impact.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir:uploads/photos}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadRoot = Paths.get(uploadDir).toAbsolutePath().getParent().toUri().toString();

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadRoot)
                .setCachePeriod(3600); // Cache for 1 hour
    }
}

