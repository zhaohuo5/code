package com.example.ssm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MyBtachInsertBean {

    @Bean
    public MyBatchInsert easySqlInjector () {
        return new MyBatchInsert();
    }

}
