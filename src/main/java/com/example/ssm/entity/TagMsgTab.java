package com.example.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 消息標簽標
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
public class TagMsgTab implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * tag主鍵
     */
    private Integer tagId;

    /**
     * 消息id
     */
    private Integer msgId;

    private Integer typeId;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "TagMsgTab{" +
        "id=" + id +
        ", tagId=" + tagId +
        ", msgId=" + msgId +
        "}";
    }
}
