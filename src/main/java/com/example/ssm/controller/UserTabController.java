package com.example.ssm.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ssm.entity.UserTab;
import com.example.ssm.service.IUserTabService;
import com.example.ssm.util.JWTUtil;
import com.example.ssm.util.MyMd5Encode;
import com.example.ssm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.UUID;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/user")
public class UserTabController {
    @Autowired
    private IUserTabService userTabService;

    @PostMapping
    public Result register(@RequestBody UserTab userTab){
        String regrex="^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        if (!userTab.getUserEmail().matches(regrex) || userTab.getUserRole()<=0) {
            return  Result.fail("邮箱验证错误，请重试");
        }
        //设置默认值
        userTab.setUserName("user"+ UUID.randomUUID());
        userTab.setGender("男");
        userTab.setSignature("");

        userTab.setUserPassword(MyMd5Encode.string2MD5(userTab.getUserPassword()));
        boolean save = userTabService.save(userTab);
        if(save) return Result.ok("sucess");
        else return  Result.fail("fail");
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserTab userTab, HttpSession session) {
        String encode = MyMd5Encode.string2MD5(userTab.getUserPassword());
        userTab.setUserPassword(encode);
        LambdaQueryWrapper<UserTab> userQuery = new LambdaQueryWrapper<>();
        userQuery.eq(UserTab::getUserEmail, userTab.getUserEmail()).eq(UserTab::getUserPassword, userTab.getUserPassword());
        UserTab one = userTabService.getOne(userQuery);
        if (one != null) {
            one.setUserPassword(null);
            session.setAttribute("user",one);
            String token = JWTUtil.getToken("heihei", (HashMap) BeanUtil.beanToMap(one));
          return   Result.ok("登录成功");
        } else return Result.fail("登录失败");

    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @GetMapping(value = "/logout")
    public Result logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return Result.ok("登出成功");
    }


}

