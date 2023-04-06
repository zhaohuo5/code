package com.example.ssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ExtendTagTabMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);
}
