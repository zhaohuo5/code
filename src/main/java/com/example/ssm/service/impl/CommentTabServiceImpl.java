package com.example.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.CommentTab;
import com.example.ssm.mapper.CommentTabMapper;
import com.example.ssm.service.ICommentTabService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service
public class CommentTabServiceImpl extends ServiceImpl<CommentTabMapper, CommentTab> implements ICommentTabService {

}
