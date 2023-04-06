package com.example.ssm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("注册");
//        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/**").excludePathPatterns(
//                "/user/login",
//                "/user/logout");
    }
}
