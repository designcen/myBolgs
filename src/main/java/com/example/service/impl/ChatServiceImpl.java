package com.example.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.lang.Constant;
import com.example.im.vo.ImMess;
import com.example.im.vo.ImUser;
import com.example.service.ChatService;
import com.example.shiro.AccountProfile;
import com.example.utils.CommonUtils;
import com.example.utils.RedisUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tio.http.common.HttpRequest;

import java.util.*;

/**
 * @author cenkang
 * @Date 2020/3/2 16:01
 */
@Service("chatService")
public class ChatServiceImpl implements ChatService {
    @Autowired
    RedisUtils redisUtils;

    @Override
    public ImUser getCurrentImUser() {
        Object o = SecurityUtils.getSubject().getPrincipal();
        ImUser user = new ImUser();
        if (o != null) {
            AccountProfile profile = new AccountProfile();
                // 虽然o与profile是同一个对象，但是此处没有用向下转型，
                // 原因是：本项目中使用了热部署spring-boot-devtools包，
                // 导致同一个类使用了不同了类加载器从而不能识别是同一个类
                // 拓展：devtools默认会对IDE中引入的所有项目使用restart类加载器，对于引入的jar包使用base类加载器。
            CommonUtils.copyProperties(profile,o);
            user.setId(profile.getId());
            user.setAvatar(profile.getAvatar());
            user.setUsername(profile.getUsername());
            user.setMine(true);
            user.setStatus(ImUser.ONLINE_STATUS);
        } else {
            user.setAvatar(Constant.DEFAULT_GARY_AVATAR);

            // 匿名用户处理
            Long imUserId = (Long) SecurityUtils.getSubject().getSession().getAttribute("imUserId");
            user.setId(imUserId != null ? imUserId : RandomUtil.randomLong());

            SecurityUtils.getSubject().getSession().setAttribute("imUserId", user.getId());

            user.setSign("never give up!");
            user.setUsername("匿名用户");
            user.setStatus(ImUser.ONLINE_STATUS);
        }

        return user;

    }

    @Override
    public String buildSystemMess(Long windowId, String content) {
        Map<Object, Object> sysMess = MapUtil.builder()
                .put("system", true)
                .put("id", windowId)
                .put("type", "group")
                .put("content", content)
                .map();

        return JSONUtil.toJsonStr(sysMess);
    }

    @Override
    public Set<Object> findAllOnlineMembers() {
        Set<Object> ids = redisUtils.sGet(Constant.IM_ONLINE_MEMBERS_KEY);

        Set<Object> results = new HashSet<>();
        if (ids == null) return results;

        ids.forEach((id) -> {

            Map<Object, Object> map = redisUtils.hmget((String) id);
            results.add(map);

        });

        return results;
    }

    @Override
    public void putOnlineMember(HttpRequest httpRequest) {
        String username = httpRequest.getParam("username");
        String userId = httpRequest.getParam("userId");
        String avatar = httpRequest.getParam("avatar");


        Map<String, Object> member = MapUtil.builder("id", (Object) userId)
                .put("username", username)
                .put("avatar", avatar)
                .map();
        redisUtils.hmset("members:" + userId, member);

        redisUtils.sSet(Constant.IM_ONLINE_MEMBERS_KEY, "members:" + userId);
    }

    @Override
    public void popOutlineMember(String userid) {
        redisUtils.setRemove(Constant.IM_ONLINE_MEMBERS_KEY, "members:" + userid);
    }

    @Override
    public void setGroupHistoryMsg(ImMess imMess) {
        // 将对象转json格式存入redis中的list结构中
        redisUtils.lSet(Constant.IM_GROUP_HISTROY_MSG_KEY, imMess, 24 * 60 * 60);
    }

    @Override
    public List<Object> getGroupHistoryMsg(int count) {
        long length = redisUtils.lGetListSize(Constant.IM_GROUP_HISTROY_MSG_KEY);
        // 将list中的对象（json格式）转成linkedHashMap格式取出来
        return redisUtils.lGet(Constant.IM_GROUP_HISTROY_MSG_KEY, length - count < 0 ? 0 : length - count, length);
    }
}
