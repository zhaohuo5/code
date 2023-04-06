package com.example.ssm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.AdminTab;
import com.example.ssm.mapper.AdminTabMapper;
import com.example.ssm.service.IAdminTabService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service
public class AdminTabServiceImpl extends ServiceImpl<AdminTabMapper, AdminTab> implements IAdminTabService {

}
