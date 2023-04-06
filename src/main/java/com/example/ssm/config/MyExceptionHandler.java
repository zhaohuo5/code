package com.example.ssm.config;

import com.example.ssm.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
@ResponseBody
public class MyExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public Result handle(SQLException sqlException){
        log.debug(sqlException.getMessage());
        return Result.fail("统一异常处理：失败");
    }
}
