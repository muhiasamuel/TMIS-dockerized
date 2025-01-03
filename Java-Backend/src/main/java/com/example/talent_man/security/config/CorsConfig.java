package com.example.talent_man.security.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableAutoConfiguration
public class CorsConfig {


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://192.168.1.112", "http://192.168.90.104","http://192.168.88.199","http://192.168.89.88","http://192.168.90.127","http://localhost")); // Allow frontend URL
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin: *"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//        config.setAllowCredentials(true); // Allow credentials if needed
        source.registerCorsConfiguration("/**", config); // Apply CORS configuration to all endpoints
        return new CorsFilter(source);
    }


}

