package com.example.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.UserTab;
import com.example.ssm.mapper.UserTabMapper;
import com.example.ssm.service.IUserTabService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service
public class UserTabServiceImpl extends ServiceImpl<UserTabMapper, UserTab> implements IUserTabService {

}
