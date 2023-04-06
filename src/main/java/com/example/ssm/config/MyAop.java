package com.example.ssm.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAop {


    public void before(){
        System.out.println("before");
    }
    public void after (){
        System.out.println("after");
    }


    public void around(){
        System.out.println("around");
    }
}


