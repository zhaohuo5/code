package com.example.ssm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ssm.entity.MessageTab;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Mapper
public interface MessageTabMapper extends BaseMapper<MessageTab> {


     List<Integer> selectBypage(int page);

    List<Integer> getMsgByTypeId(HashMap typeIds);


    List<Integer> getSingleMsgIdByTypeId  (@Param("typeids") Set<Integer> typeIds);
    List<MessageTab> getPageByTypeId  (@Param("typeids") Set<Integer> typeIds,@Param("page")int page,@Param("offset") int offeset);

}
