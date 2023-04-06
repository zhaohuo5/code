package com.example.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTab implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户email
     */
    private String userEmail;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户性别
     */
    private String gender;

    /**
     * 用户签名
     */
    private String signature;

    /**
     * 1代表娱乐新闻爱好者  0代表财金爱好者
     */
    private Integer userRole;


}
