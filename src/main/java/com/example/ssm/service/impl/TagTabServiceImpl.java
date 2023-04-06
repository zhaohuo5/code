package com.example.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.RoleTab;
import com.example.ssm.entity.TagMsgTab;
import com.example.ssm.entity.TagTab;
import com.example.ssm.mapper.MessageTabMapper;
import com.example.ssm.mapper.RoleTabMapper;
import com.example.ssm.mapper.TagMsgTabMapper;
import com.example.ssm.mapper.TagTabMapper;
import com.example.ssm.service.ITagTabService;
import com.example.ssm.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service
public class TagTabServiceImpl extends ServiceImpl<TagTabMapper, TagTab> implements ITagTabService {

    @Autowired
    private RoleTabMapper roleTabMapper;
    @Autowired
    private MessageTabMapper messageTabDao;
    @Autowired
    private TagMsgTabMapper tagMsgTabDao;
    @Autowired
    private TagTabMapper tagTabDao;

    public Result geTopTags(int roleId) {

        LambdaQueryWrapper<RoleTab> roleQuery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TagMsgTab> tagQuery = new LambdaQueryWrapper<>();

        roleQuery.select(RoleTab::getTypeId).eq(RoleTab::getRoleId,roleId);
        Set<Integer> typeIds = this.roleTabMapper.selectList(roleQuery).stream().map(s -> s.getTypeId()).collect(Collectors.toSet());
        if(typeIds==null){
            return Result.fail("当前权限无消息可查看");
        }
        //根据typeID进行查找
        List<Integer> msgIds = this.messageTabDao.selectBatchIds(typeIds).stream().map(s -> s.getId()).collect(Collectors.toList());
        //根据msg ID 进行tag id的统计
        //   select  tag_id  from tag_msg_tab where  msg_id in (1,2,3,4,5,6,7,8,9,10);
        tagQuery.in(TagMsgTab::getMsgId,msgIds);
        List<Integer> tagIds = this.tagMsgTabDao.selectList(tagQuery).stream().map(s -> s.getTagId()).collect(Collectors.toList());

        //----- 进行tagid 的统计

        LambdaQueryWrapper<TagTab> tagUqery = new LambdaQueryWrapper<>();
        tagUqery.in(TagTab::getId,tagIds);
//        获取tag 的id与name的映射关系
        Map<Integer, String> tagMaps = this.tagTabDao.selectList(tagUqery).stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getTagName()));

        Map<String, Long> collect = tagIds.stream().collect(Collectors.groupingBy(s -> tagMaps.get(s), Collectors.counting()));
        Map<String, Long> sorted = collect.entrySet().stream()
                .sorted(Map.Entry.<String,Long>comparingByValue().reversed())   .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));

        return  Result.ok(sorted);

    }
}
