package com.example.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ssm.entity.TagMsgTab;
import com.example.ssm.util.Result;

/**
 * <p>
 * 消息標簽標 服务类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
public interface ITagMsgTabService extends IService<TagMsgTab> {
    Result geTopTags(int role);
}
