package com.example.ssm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ssm.entity.MessageTab;
import com.example.ssm.entity.wrapper.MyQueryWraaper;
import com.example.ssm.entity.wrapper.RecievedMessage;
import com.example.ssm.util.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
public interface IMessageTabService extends IService<MessageTab> {
    Result getBypage(int pages);
    Result userGetMsgByAuth(MyQueryWraaper map);
    Result adminGetMsgByAuth(MyQueryWraaper map);

    Result updateMsg(RecievedMessage recievedMessage);

    Result getMsgDetail(int id,int page);
}
