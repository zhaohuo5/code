package com.example.ssm.controller;


import com.example.ssm.service.ITagMsgTabService;
import com.example.ssm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 消息標簽標 前端控制器
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/tagMsgTab")
public class TagMsgTabController {
    @Autowired
    private ITagMsgTabService tagMsgTabService;
    @GetMapping("/getTopTag/{role}")
    public Result getTopTag(@PathVariable int role){
        if(role<=0){
            return  Result.fail("权限错误，请重试");
        }
      Result result=  tagMsgTabService.geTopTags(role);

        return  result;

    }

}

