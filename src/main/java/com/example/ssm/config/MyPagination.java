package com.example.ssm.config;

import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyPagination {


    @Bean
    public PaginationInnerInterceptor handlePage(){
        System.out.println("分页处理");
        return new PaginationInnerInterceptor();


    }
}
