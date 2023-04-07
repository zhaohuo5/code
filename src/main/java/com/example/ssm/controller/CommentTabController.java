package com.example.ssm.controller;


import com.example.ssm.entity.CommentTab;
import com.example.ssm.service.ICommentTabService;
import com.example.ssm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@RestController
@RequestMapping("/commentTab")
public class CommentTabController {

    @Autowired
    private ICommentTabService commentTabService;
    @PostMapping
    Result InsertComment(@RequestBody CommentTab commentTab){
        if(commentTab.getUserId()==null||commentTab.getMsgId()==null){
            return  Result.fail("错误的ID，请重试");
        }
        LocalDateTime now = LocalDateTime.now();
        commentTab.setCommentTime(now);
        boolean save = commentTabService.save(commentTab);
        if (save) {
            return Result.ok("插入成功");
        }
        return Result.fail("插入失败");

    }

}

