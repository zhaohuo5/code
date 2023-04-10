package com.example.ssm.controller;


import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpResponse;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ssm.entity.MessageTab;
import com.example.ssm.entity.UserTab;
import com.example.ssm.entity.wrapper.MyQueryWraaper;
import com.example.ssm.entity.wrapper.RecievedMessage;
import com.example.ssm.service.IMessageTabService;
import com.example.ssm.service.IUserTabService;
import com.example.ssm.util.Result;
import com.example.ssm.util.UserConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */

@RestController
@Slf4j
@RequestMapping("/messageTab")
public class MessageTabController {
    @Resource
    private IMessageTabService messageTabService;
    @Autowired
    private IUserTabService userTabService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${fileUpload.path}")
    String path;
    @PostMapping("/upload")
    public Result uploadFile(MultipartFile file) {

        String filename= UUID.randomUUID(true)+"";


        String subfix = file.getOriginalFilename().split("\\.")[1];
        filename=filename+"."+subfix;
       String finalpath= path+File.separator+filename;
        try {
            file.transferTo(new File(finalpath));
        } catch (IOException e) {
            log.error("文件上传失败，请重试");
            e.printStackTrace();
        }
        return Result.ok(filename);
    }

    @GetMapping("download")
    public Result downloadFile(String name, HttpServletResponse response) {
        BufferedInputStream fileInputStream = null;
        try {
            fileInputStream = new BufferedInputStream(new FileInputStream(path + File.separator+ name));
            byte[] bytes = new byte[1024];
            int len = 0;
            ServletOutputStream outputStream=null;
            while ((len = fileInputStream.read(bytes)) != -1) {
                 outputStream = response.getOutputStream();
                outputStream.write(bytes, 0, len);

            }
            outputStream.flush();
            outputStream.close();
            response.setContentType("image/jpeg");

        } catch (Exception e) {
            e.printStackTrace();

        }finally {

           if(fileInputStream!=null) {
               try {
                   fileInputStream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
        }
        return Result.ok("下载成功");

    }

    @GetMapping("/select")
    public Result getPage(MyQueryWraaper map, HttpSession session) {

        long start = System.currentTimeMillis();
//        设置默认值
        if (map.getTypeId() <= 0) {
            map.setTypeId(-1);

        }
        if (map.getTagId() <= 0) {
            map.setTagId(-1);

        }

        if (map.getPage() <= 0) {
            map.setPage(-1);

        }
//        根据session获取用户ID
//        UserTab user =(UserTab) session.getAttribute("user");
//        if(user.getId()<=0){
//         return  Result.fail("请登录");
//        }
//        Integer userId = ( user.getId());

        Integer userId = 1;
        LambdaQueryWrapper<UserTab> userTabLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userTabLambdaQueryWrapper.select(UserTab::getUserRole).eq(UserTab::getId, userId);
        //增加缓存
        String role = stringRedisTemplate.opsForValue().get(UserConstant.USERROLE + userId);
        if (StringUtils.isEmpty(role)) {
            role = userTabService.getOne(userTabLambdaQueryWrapper).getUserRole() + "";
            stringRedisTemplate.opsForValue().set(UserConstant.USERROLE + userId, userId + "", 30, TimeUnit.MINUTES);
        }
        map.setRole(Integer.valueOf(role));
        Result result;
        if (Integer.valueOf(role) == 66) {
            //根据用户权限判断查询
            result = messageTabService.adminGetMsgByAuth(map);
        } else {
            result = messageTabService.userGetMsgByAuth(map);
        }

//        long end = System.currentTimeMillis();
//        if((end-start)>1000){
//            System.out.println(Thread.currentThread().getName());
//        }


        return result;
    }


    @PostMapping
    public Result updateMsg(@RequestBody RecievedMessage recievedMessage) {
        // 一个是message对象
        if (recievedMessage.getId() == null) {
            return Result.fail("请求失败，请传入id");
        }
        Result result = this.messageTabService.updateMsg(recievedMessage);
        // 一个是tag 数组

        return result;

    }

    @DeleteMapping()
    public Result delteMsg(@RequestBody MessageTab messageTab) {
        // 一个是message对象
        boolean b = this.messageTabService.removeById(messageTab.getId());
        if (b){
            return Result.ok("删除成功");
        }
        else {
            return Result.fail("删除失败");
        }


    }

    @GetMapping("/{id}/{page}")
    public Result getDetail(@PathVariable int id, @PathVariable int page) {
        if (id <= 0 || page <= 0) {
            return Result.fail("请输入正确的参数");
        }


        // 一个是message对象
        Result result = this.messageTabService.getMsgDetail(id, page);

        return result;

    }


}

