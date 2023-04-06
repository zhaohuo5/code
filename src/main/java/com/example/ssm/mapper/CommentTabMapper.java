package com.example.ssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ssm.entity.CommentTab;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Mapper
public interface CommentTabMapper extends BaseMapper<CommentTab> {
    List<CommentTab> getComment(@Param("msgId") int msgId,@Param("page") int page);

}
