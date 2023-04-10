package com.example.ssm.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssm.entity.*;
import com.example.ssm.entity.wrapper.MyQueryWraaper;
import com.example.ssm.entity.wrapper.RecievedMessage;
import com.example.ssm.entity.wrapper.WrMessageTab;
import com.example.ssm.mapper.*;
import com.example.ssm.service.IMessageTabService;
import com.example.ssm.util.Result;
import com.example.ssm.util.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aliang
 * @since 2023-03-21
 */
@Service

public class MessageTabServiceImpl extends ServiceImpl<MessageTabMapper, MessageTab> implements IMessageTabService {
    @Resource
    private MessageTabMapper messageTabDao;
    @Resource
    private TypeTabMapper typeTabDao;
    @Resource
    private TagMsgTabMapper tagMsgTabDao;
    @Resource
    private TagTabMapper tagTabDao;
    @Resource
    RoleTabMapper roleTabMapper;
    @Resource
    CommentTabMapper commentTabMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result adminGetMsgByAuth(MyQueryWraaper map){
        /*
        *先查role 对应的type 在去查tagmsgtab 对应的msgiD 最后再查msg信息
        *
        * */
        int tagId=map.getTagId();
        int typeId = map.getTypeId();
        int offset = map.getOffset();
        int page=(map.getPage()-1)*offset;
        String limit="limit "+page+","+offset;
        LambdaQueryWrapper<TagMsgTab> tagQuery = new LambdaQueryWrapper<>();
        tagQuery.select(TagMsgTab::getMsgId)
                .eq(tagId>0,TagMsgTab::getTagId,tagId)
                .in(typeId>0,TagMsgTab::getTypeId,typeId).last(limit);
        List<TagMsgTab> tagMsgTabs = tagMsgTabDao.selectList(tagQuery);
        if(tagMsgTabs.size()==0) {
            return Result.fail("没有符合的消息");
        }
        List<Integer> msgIds = tagMsgTabs.stream().map(s -> s.getMsgId()).collect(Collectors.toList());


//        Result result = getpageByMsgId(msgIds);



        return null;
    }

    @Override
    public Result updateMsg(RecievedMessage messageTab) {

        LocalDateTime now = LocalDateTime.now();
        messageTab.setMtime(now);
        int msgId=messageTab.getId();
        // 传尽来tagId的数组
        List<Integer> newIds = messageTab.getTagIds();
        messageTabDao.updateById(messageTab);

        if (newIds==null) {
            // 如果tag为0 代表全删除，或者不需要修改
           return Result.ok("修改成功");
        }
        LambdaQueryWrapper<TagMsgTab> tagmsgQuery = new LambdaQueryWrapper<>();
        tagmsgQuery.eq(TagMsgTab::getMsgId, msgId);





        LambdaQueryWrapper<TagMsgTab> tagmsgtable = new LambdaQueryWrapper<>();
        tagmsgtable.select(TagMsgTab::getTagId).eq(TagMsgTab::getMsgId, msgId);
        List<Integer> oldIds = tagMsgTabDao.selectList(tagmsgtable).stream().map(s->s.getTagId()).collect(Collectors.toList());
        //获取交集
        ArrayList<Integer> mix = new ArrayList<>(newIds);
        mix.retainAll(oldIds);
        //旧的的拿来删除
        oldIds.removeAll(mix);

//        如果说==0 代表不需要删除，那么进行
        // old-mix=delete
        if (oldIds.size()> 0) {
            //进行旧id的删除
            LambdaQueryWrapper<TagMsgTab> deleteQuery = new LambdaQueryWrapper<>();
            //根据旧id 进行删除
            deleteQuery.eq(TagMsgTab::getMsgId,msgId).in(TagMsgTab::getTagId,oldIds);
            tagMsgTabDao.delete(deleteQuery);

        }
        //进行新id的添加   new-mix= add
        newIds.removeAll(mix);
        //如果是0 代表已经完成tag的更新
        if(newIds.size()==0) {

            return Result.ok("修改成功");
        }
        ArrayList<TagMsgTab> tagMsgTabs = new ArrayList<>();
        for (Integer newId : newIds) {
            TagMsgTab temp = new TagMsgTab();
            temp.setMsgId(msgId);
            temp.setTagId(newId);
            temp.setTypeId(messageTab.getTypeId());
            tagMsgTabs.add(temp);

        }
        //批量添加
        tagMsgTabDao.insertBatchSomeColumn(tagMsgTabs);

        return Result.ok("修改成功");
    }

    @Override
    public Result getMsgDetail(int id,int page) {
        page=(page-1)*200;
        // 判断当前消息是否存在缓存
        String s1 = stringRedisTemplate.opsForValue().get(id+"");
        if(!StrUtil.isEmpty(s1)){
            RecievedMessage recievedMessage = JSONUtil.toBean(s1, RecievedMessage.class);
            return  Result.ok(recievedMessage);
        }
        LambdaQueryWrapper<MessageTab> megquery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TypeTab> typequery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TagMsgTab> tagmsgQuery = new LambdaQueryWrapper<>();
        HashMap <Integer,String> tagsGroup = new HashMap();
        megquery.eq(MessageTab::getId,id).select(
                MessageTab::getMsg,MessageTab::getTypeId,
                MessageTab::getMsg);
        MessageTab messageTab = messageTabDao.selectOne(megquery);
        typequery.eq(TypeTab::getId,messageTab.getTypeId());
        Map<Integer, String> typeMap = typeTabDao.selectList(typequery).stream().collect(Collectors.toMap(s -> s.getId(), s -> s.getTypeName()));
        tagmsgQuery.select(TagMsgTab::getTagId).eq(TagMsgTab::getMsgId,id);
        List<Integer> tagIds = tagMsgTabDao.selectList(tagmsgQuery).stream().map(s-> s.getTagId()).collect(Collectors.toList());
        List<String> tagNames = tagTabDao.selectBatchIds(tagIds).stream().map(s->s.getTagName()).collect(Collectors.toList());

        for (int i = 0; i < tagNames.size(); i++) {
            tagsGroup.put(tagIds.get(i),tagNames.get(i));
        }




        List<CommentTab> commentTabs= this.commentTabMapper.getComment( id,page);
        RecievedMessage data = new RecievedMessage(messageTab, tagsGroup, typeMap.get(messageTab.getTypeId()), commentTabs);
        stringRedisTemplate.opsForValue().set(id+"",JSONUtil.toJsonStr(data),5,TimeUnit.MINUTES);
        return  Result.ok(data);
    }


    @Override
    public Result getBypage(int pages) {
        return null;
    }

    @Override
    public Result userGetMsgByAuth(MyQueryWraaper map) {
        long start = System.currentTimeMillis();
        map.setPage((map.getPage()-1)*200);

        LambdaQueryWrapper<RoleTab> roleQuery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TagMsgTab> tagQuery = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<MessageTab> messageTabLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //根据roleid 和typeid进行查询
        roleQuery.select(RoleTab::getTypeId).eq(RoleTab::getRoleId,map.getRole());
        //根据权限获取到对应 typeid
        Set<Integer> typeIds;
        //是否指定了type   如果指定不需要根据权限查找对应的type
        //循环3-10次
        typeIds = roleTabMapper.selectList(roleQuery).stream().map(s -> s.getTypeId()).collect(Collectors.toSet());
        // != -1代表用户指定了typeID 进行查找
        if(map.getTypeId()!=-1){
            if (!typeIds.contains(map.getTypeId())) {
                return Result.fail("无权限查询");}

        }
        //如果type是空的话
        if(typeIds.isEmpty()){
            return Result.fail("无可查看的类型");
        }

        //根据typeid 获取对应的分页后的msg id
        // 1


        long tagStart = System.currentTimeMillis();



        List<MessageTab> messageTabs;
        String limit="limit "+map.getPage()+","+"200";
        if(map.getTagId()<=0){
            messageTabs = this.messageTabDao.getPageByTypeId(typeIds, map.getPage(), UserConstant.OFFSET);
//            msgIds=messageTabs.stream().map(s->s.getId()).collect(Collectors.toList());

        }else{

            tagQuery.select(TagMsgTab::getMsgId).
                    eq(TagMsgTab::getTagId,map.getTagId()).in(map.getTypeId()<=0,TagMsgTab::getTypeId,typeIds).last(limit);

            List<Integer> msgIds = tagMsgTabDao.selectList(tagQuery).stream().map(s -> s.getMsgId()).collect(Collectors.toList());
            messageTabLambdaQueryWrapper.select(MessageTab::getId,
                    MessageTab::getMsg,
                    MessageTab::getCtime,
                    MessageTab::getMtime,
                    MessageTab::getTypeId).in(MessageTab::getId,msgIds);
             messageTabs = messageTabDao.selectList(messageTabLambdaQueryWrapper);
        }




        //根据msgId  获取对应tag id
        // 再根据 tag id 获取
        if(messageTabs.size()==0) {
            return Result.fail("查无此消息");
        }



        Result result = getpageByMsgId(messageTabs);
//        long end1 = System.currentTimeMillis();
        long end = System.currentTimeMillis();
//        if(end-start>1000){
//            System.out.println("great than 1 is :"+(end-start));
//            System.out.println("tag end"+(tagEnd-tagStart));
//
//        }

        return result;
    }







    private Result getpageByMsgId(List<MessageTab> messageTabs){

        HashMap<Integer, HashMap<Integer,String>> tagIdsGroupByMsgId = new HashMap<>();
        QueryWrapper<TagMsgTab> tagMsgTabQueryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<MessageTab> mesQuery = new LambdaQueryWrapper<>();

//        mesQuery.select(MessageTab::getId,
//                MessageTab::getMsg,
//                MessageTab::getTypeId,
//                MessageTab::getMtime,
//                MessageTab::getCtime).in(MessageTab::getId,msgIds);
//        long start = System.currentTimeMillis();
//        List<MessageTab> messageTabs = messageTabDao.selectList(mesQuery);
//        System.out.println("msg数据库查询"+(System.currentTimeMillis()-start));
        Set<Integer> typeIds = messageTabs.stream().map(s -> s.getTypeId()).collect(Collectors.toSet());
        Set<Integer> typeIdsBack = new HashSet<>(typeIds);
        int setSize=typeIds.size();

        //判断是否存在map的映射
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(UserConstant.TYPEID);
        Map<Object, String> typeMap ;
        if(!entries.isEmpty()){
            typeMap= new HashMap<>();
           Iterator<Integer> iterator = typeIdsBack.iterator();
           while (iterator.hasNext()){
               String id=iterator.next()+"";

               if (entries.containsKey(id+"")) {
                   //把映射的数据进行删除，
                   iterator.remove();
                   typeMap.put(id,entries.get(id)+"");
               }
           }
            //判断 如果 typeids 的size和typeMap的size 不一样 查询数据库 获取完整的type name 并加入到缓存中
        if (setSize!=typeMap.size()){

            Map<Integer, String> collect = typeTabDao.selectBatchIds(typeIdsBack).stream().collect(Collectors.toMap((s -> s.getId()), s -> s.getTypeName()));
            // 循环添加 到缓存 和typeMap 中
            Iterator<Integer> iterator2 = typeIdsBack.iterator();
            //把缺少的添加到typeMap里面
            while (iterator2.hasNext()){
                int id=iterator2.next();
                typeMap.put(id,collect.get(id));
                // 数据添加，以便添加到缓存中
                entries.put(id+"",collect.get(id));
            }
            //添加到缓存中
            stringRedisTemplate.opsForHash().putAll(UserConstant.TYPEID,entries);
            stringRedisTemplate.expire(UserConstant.TYPEID,30,TimeUnit.MINUTES);

        }
       }else{
            //redis miss , query db and add to redis
            List<TypeTab> typeTabs = typeTabDao.selectBatchIds(typeIds);
            typeMap = typeTabs.stream().collect(Collectors.toMap(s -> s.getId(), TypeTab::getTypeName));
            Map<String, String> collect = typeTabs.stream().collect(Collectors.toMap(s -> s.getId() + "", TypeTab::getTypeName));


            stringRedisTemplate.opsForHash().putAll(UserConstant.TYPEID,collect);
        }




        //获取 全部的 type id集合,用此进行集合的映射

        //用去重后的typeid 进行查询，获取 id和typen name 的映射关系


        // transforme into map

        tagMsgTabQueryWrapper.select("distinct tag_id").in("type_id",typeIds);
//        long start = System.currentTimeMillis();
        //获取tag ID
        Set<Integer> tagIds = this.tagMsgTabDao.selectList(tagMsgTabQueryWrapper).stream().map(s->s.getTagId()).collect(Collectors.toSet());
        Set<Integer> tagIdsBack = new HashSet<>(tagIds);
        //去重 后获取type name
        if(tagIds.isEmpty()){
            return Result.fail("查无此消息");
        }


        //2 get tag id 并去除重复，进行数据的映射

        //先去缓存中取tagMap ,
        Map<Object, Object> tagMap = stringRedisTemplate.opsForHash().entries(UserConstant.TAGMAP);
        Map<String, String> tagResultMap = new HashMap<>();
        // cashe miss
        if(tagMap.isEmpty()){
            List<TagTab> tagTabs = tagTabDao.selectBatchIds(tagIds);
           tagMap = tagTabs.stream().collect(Collectors.toMap(s -> s.getId()+"", s -> s.getTagName()));
           //
           tagResultMap = tagTabs.stream().collect(Collectors.toMap(s -> s.getId()+"", s -> s.getTagName()));
           //加入缓存
           stringRedisTemplate.opsForHash().putAll(UserConstant.TAGMAP,tagMap);
        }else{
            //cache 命中
            Iterator<Integer> iterator = tagIdsBack.iterator();
            while (iterator.hasNext()){
                String next = iterator.next()+"";
                if(tagMap.containsKey(next)){
                    // 如果取出来的Map中包含了当前的key，那么说明命中，直接添加到tagMap集合中即可
                    tagResultMap.put(next,tagMap.get(next)+"");
                    iterator.remove();
                }
            }
        }
        //不等于，说明未能完全命中还是查找一次数据库
        if(tagMap.size()!=tagResultMap.size()){

            LambdaQueryWrapper<TagMsgTab> tagMsgTabLambdaQueryWrapper = new LambdaQueryWrapper<>();
            tagMsgTabLambdaQueryWrapper.select(TagMsgTab::getMsgId,TagMsgTab::getTagId).in(TagMsgTab::getTagId,tagIdsBack);
            //查询出来循环添加进去,差别的添加到缓存中去
            Map<Integer, Integer> diff = tagMsgTabDao.selectList(tagMsgTabLambdaQueryWrapper).stream().collect(Collectors.toMap(s -> s.getMsgId(), s -> s.getTagId()));
            Iterator<Integer> iterator = tagIdsBack.iterator();
            while (iterator.hasNext()){
                String id=iterator.next()+"";
                tagMap.put(id,diff.get(id));
                tagResultMap.put(id,diff.get(id)+"");

                //更新缓存
                stringRedisTemplate.opsForHash().putAll(UserConstant.TAGMAP,tagMap);
                stringRedisTemplate.expire(UserConstant.TAGMAP,30,TimeUnit.MINUTES);
            }


        }



//        List<TagTab> tagTabs = tagTabDao.selectBatchIds(tagIds);
        //映射
        LambdaQueryWrapper<TagMsgTab> tagQuery = new LambdaQueryWrapper<>();
        List<Integer> msgIds = messageTabs.stream().map(s -> s.getId()).collect(Collectors.toList());
        tagQuery.select(TagMsgTab::getMsgId,TagMsgTab::getTagId).in(TagMsgTab::getMsgId,msgIds);



        List<TagMsgTab> tagMsgTabs = tagMsgTabDao.selectList(tagQuery);
    // 进行 tagID 和name的绑定
        for (int i = 0; i < tagMsgTabs.size(); i++) {
            Integer msgId = tagMsgTabs.get(i).getMsgId();
            Integer tagId = tagMsgTabs.get(i).getTagId();
            String tag =tagResultMap.get(tagMsgTabs.get(i).getTagId()+"");
            if(tagIdsGroupByMsgId.containsKey(msgId)){
                tagIdsGroupByMsgId.get(msgId).put(tagId,tag);
            }else{ // first ,need to new list
                //tagids

                HashMap<Integer, String> maps = new HashMap<>();
                maps.put(tagId,tag);


                //new list and add tagId into list
                tagIdsGroupByMsgId.put(msgId,maps);
            }
            // 添加到tagid  里面 判断是否重复

        }
        List<RecievedMessage> collect =
                messageTabs.stream().map(s -> new RecievedMessage(s, tagIdsGroupByMsgId.get(s.getId()) , typeMap.get(s.getTypeId()))).collect(Collectors.toList());


        //重新封装成一个对象

        Collections.sort(collect, Comparator.comparing(RecievedMessage::getLmtime));
        return Result.ok(collect,(long)collect.size());
    }


}
