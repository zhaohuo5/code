package com.example.ssm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@AllArgsConstructor
@NoArgsConstructor
public class MessageTab  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发布者id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;




    /**
     * 发布者id
     */
    private Integer authorId;

    /**
     * 类型Id
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Integer typeId;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 创建时间
     */
//    @JsonFormat (shape = JsonFormat.Shape.NUMBER_INT)
    private LocalDateTime ctime;

    /**
     * 状态,构建索引
     */
    @TableLogic(value = "1",delval = "0")
    private Integer msgStatus;

    /**
     * 修改
     */

    private LocalDateTime mtime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LocalDateTime getCtime() {
        return ctime;
    }

    public void setCtime(LocalDateTime ctime) {
        this.ctime = ctime;
    }

    public Integer getMsgStatus() {
        return msgStatus;
    }

    public void setMsgStatus(Integer msgStatus) {
        this.msgStatus = msgStatus;
    }

    public LocalDateTime getMtime() {
        return mtime;
    }

    public void setMtime(LocalDateTime mtime) {
        this.mtime = mtime;
    }

    @Override
    public String toString() {
        return "MessageTab{" +
        "id=" + id +
        ", authorId=" + authorId +
        ", typeId=" + typeId +
        ", msg=" + msg +
        ", ctime=" + ctime +
        ", msgStatus=" + msgStatus +
        ", mtime=" + mtime +
        "}";
    }
}
