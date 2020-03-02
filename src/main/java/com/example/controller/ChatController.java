package com.example.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.lang.Constant;
import com.example.common.lang.Result;
import com.example.im.vo.ImUser;
import com.example.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cenkang
 * @Date 2020/3/2 15:57
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController{
    @Autowired
    ChatService chatService;

    @GetMapping("/getMineAndGroupData")
    public Result getMineAndGroupData(HttpServletRequest request) {

        //获取用户信息
        ImUser user = chatService.getCurrentImUser();

        //默认群
        Map<String, Object> group = new HashMap<>();
        group.put("name", "社区群聊");
        group.put("type", "group");
        group.put("avatar", "http://tp1.sinaimg.cn/5619439268/180/40030060651/1");
        group.put("id", Constant.IM_DEFAULT_GROUP_ID);
        group.put("members", 0);

        return Result.succ(MapUtil.builder()
                .put("mine", user)
                .put("group", group)
                .map());
    }

    @RequestMapping("/getMembers")
    public Result getMembers() {

        Set<Object> members = chatService.findAllOnlineMembers();
        log.info("获取群成员---------->" + JSONUtil.toJsonStr(members));

        return Result.succ(MapUtil.of("list", members));
    }

    @GetMapping("/getGroupHistoryMsg")
    public Result getGroupHistoryMsg() {

        List<Object> messages = chatService.getGroupHistoryMsg(20);
        return Result.succ(messages);
    }
}
