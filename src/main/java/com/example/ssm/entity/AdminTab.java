package com.example.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
public class AdminTab implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String adminName;

    /**
     * 用户名
     */
    private String adminUsername;

    /**
     * 密码
     */
    private String adminPasword;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPasword() {
        return adminPasword;
    }

    public void setAdminPasword(String adminPasword) {
        this.adminPasword = adminPasword;
    }

    @Override
    public String toString() {
        return "AdminTab{" +
        "id=" + id +
        ", adminName=" + adminName +
        ", adminUsername=" + adminUsername +
        ", adminPasword=" + adminPasword +
        "}";
    }
}
