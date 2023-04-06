package com.example.ssm.controller;


import com.example.ssm.entity.MessageTab;
import com.example.ssm.entity.UserTab;
import com.example.ssm.entity.wrapper.MyQueryWraaper;
import com.example.ssm.entity.wrapper.RecievedMessage;
import com.example.ssm.service.IMessageTabService;
import com.example.ssm.service.IUserTabService;
import com.example.ssm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/messageTab")
public class MessageTabController {
    @Autowired
    private IMessageTabService messageTabService;
    @Autowired
    private IUserTabService userTabService;

    @GetMapping("/select")
    public Result getAdminMsgByPage(MyQueryWraaper map, HttpSession session){

        long start = System.currentTimeMillis();
//        设置默认值
        if (map.getTypeId()<=0) {
        map.setTypeId(-1);

        }
        if (map.getTagId()<=0) {
    map.setTagId(-1);

        }

        if (map.getPage()<=0) {
            map.setPage(-1);

        }
//        根据session获取用户ID
//        UserTab user =(UserTab) session.getAttribute("user");
//        if(user.getId()<=0){
//         return  Result.fail("请登录");
//        }
//        Integer userId = ( user.getId());
        //跳过登录直接查询
        Integer userId = 1;

        Integer role = userTabService.getById(userId).getUserRole();

        map.setRole(role);
        Result result;
        if(role==66){
            //根据用户权限判断查询
             result = messageTabService.adminGetMsgByAuth(map);
        }else{
              result=messageTabService.userGetMsgByAuth(map);
        }

        long end = System.currentTimeMillis();
//        if((end-start)>1000){
//            System.out.println(Thread.currentThread().getName());
//        }


        return  result;
    }



    @PostMapping
    public Result updateMsg(@RequestBody RecievedMessage recievedMessage){
        // 一个是message对象
       if(recievedMessage.getId()==null){
           return  Result.fail("请求失败，请传入id");
       }
        Result result = this.messageTabService.updateMsg(recievedMessage);
        // 一个是tag 数组

        return result;

    }
    @DeleteMapping()
    public Result delteMsg(@RequestBody MessageTab messageTab){
        // 一个是message对象
        boolean b = this.messageTabService.removeById(messageTab.getId());
        if(b)  return Result.ok("删除成功");
        else return Result.fail("删除失败");


    }

    @GetMapping("/{id}/{page}")
    public Result getDetail(@PathVariable int id,@PathVariable int page){
        if(id<=0 || page<=0){
            return Result.fail("请输入正确的参数");
        }


        // 一个是message对象
       Result result= this.messageTabService.getMsgDetail(id,page);

    return  result;

    }


}

