package com.example.ssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ssm.entity.UserTab;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Mapper
public interface UserTabMapper extends BaseMapper<UserTab> {

}
