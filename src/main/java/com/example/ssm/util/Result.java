package com.example.ssm.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private boolean success;
    private Object data;
    private String errorMsg;
    private  Long total ;
    public  static  Result ok(Object data){
        return new Result(true,data,null,null);
    }
    public  static  Result ok(List<?> data,Long total){
        return new Result(true,data,null,total);
    }
    public  static  Result fail(String error){
        return new Result(false,null,error,null);
    }
    public  static  Result ok(){
        return new Result(true,null,null,null);
    }
}
