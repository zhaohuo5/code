package com.example.ssm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.MessageTab;
import com.example.ssm.entity.RoleTab;
import com.example.ssm.entity.TagMsgTab;
import com.example.ssm.entity.TagTab;
import com.example.ssm.mapper.MessageTabMapper;
import com.example.ssm.mapper.RoleTabMapper;
import com.example.ssm.mapper.TagMsgTabMapper;
import com.example.ssm.mapper.TagTabMapper;
import com.example.ssm.service.ITagMsgTabService;
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
 * 消息標簽標 服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service
public class TagMsgTabServiceImpl extends ServiceImpl<TagMsgTabMapper, TagMsgTab> implements ITagMsgTabService {

    @Autowired
    private RoleTabMapper roleTabMapper;
    @Autowired
    private MessageTabMapper messageTabDao;
    @Autowired
    private TagMsgTabMapper tagMsgTabDao;
    @Autowired
    private TagTabMapper tagTabDao;

    public Result geTopTags(int roleId) {

        QueryWrapper<RoleTab> roleQuery = new QueryWrapper<>();
        roleQuery.select("distinct type_id").eq("role_id",roleId);

        QueryWrapper<TagMsgTab> tagQuery = new QueryWrapper<>();
        LambdaQueryWrapper<MessageTab> msgQuery = new LambdaQueryWrapper<>();



        Set<Integer> typeIds = this.roleTabMapper.selectList(roleQuery).stream().map(s -> s.getTypeId()).collect(Collectors.toSet());
        //根据typeID进行查找
//        msgQuery.select(MessageTab::getId).in(MessageTab::getTypeId,typeIds);
//        List<Integer> msgIds = this.messageTabDao.selectList(msgQuery).stream().map(s -> s.getId()).collect(Collectors.toList());
//        //根据msg ID 进行tag id的统计
//     //   select  tag_id  from tag_msg_tab where  msg_id in (1,2,3,4,5,6,7,8,9,10);
//        tagQuery.in(TagMsgTab::getMsgId,msgIds);
//        List<Integer> tagIds = this.tagMsgTabDao.selectList(tagQuery).stream().map(s -> s.getTagId()).collect(Collectors.toList());

        //----- 进行tagid 的统计
        if(typeIds.isEmpty()){
            return Result.fail("暂无tag标记趋势");
        }
        //注意这里 是使用了 msgId 接收了统计后hots的数值，因为不想重新设置一个新的成员变量
        tagQuery.select("tag_id,count(*) as msgId").in("type_id",typeIds).groupBy("tag_id");
        List<TagMsgTab> tagMsgTabs = this.tagMsgTabDao.selectList(tagQuery);

        Set<Integer> tagIds =tagMsgTabs.stream().map(s -> s.getTagId()).collect(Collectors.toSet());
        //这里就用msgId 进行map的映射
        Map<Integer, Integer> beforeSorting = tagMsgTabs.stream().collect(Collectors.toMap(s -> s.getTagId(), s -> s.getMsgId()));

        LambdaQueryWrapper<TagTab> tagUqery = new LambdaQueryWrapper<>();

        tagUqery.in(TagTab::getId,tagIds);
//        获取tag 的id与name的映射关系
        Map<Integer, String> tagMaps = this.tagTabDao.selectList(tagUqery).stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getTagName()));
        Map<String, Integer> afterMaps = beforeSorting.entrySet().stream().collect(Collectors.toMap(s -> tagMaps.get(s.getKey()), s -> s.getValue()));
        Map<String, Integer> sorted = afterMaps.entrySet().stream()
                .sorted(Map.Entry.<String,Integer>comparingByValue().reversed())   .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new));
        Result.ok(sorted);

        return  Result.ok(sorted);

    }
}
