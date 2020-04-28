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
 * 群聊信息控制
 * @author cenkang
 * @Date 2020/3/2 15:57
 */
@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController extends BaseController{

    /**
     * 获取当前用户的和当前群数据
     * @param request
     * @return
     */
    @GetMapping("/getMineAndGroupData")
    public Result getMineAndGroupData(HttpServletRequest request) {

        // 获取用户信息
        ImUser user = chatService.getCurrentImUser();

        // 默认群
        Map<String, Object> group = new HashMap<>();
        group.put("name", "社区群聊");
        group.put("type", "group");
//        group.put("avatar", "http://tp1.sinaimg.cn/5619439268/180/40030060651/1");
        group.put("avatar", Constant.DEFAULT_GARY_AVATAR);
        // 群聊默认id
        group.put("id", Constant.IM_DEFAULT_GROUP_ID);
        group.put("members", 0);

        return Result.succ(MapUtil.builder()
                .put("mine", user)
                .put("group", group)
                .map());
    }

    /**
     * 获取所有在线群聊成员
     * @return
     */
    @RequestMapping("/getMembers")
    public Result getMembers() {

        Set<Object> members = chatService.findAllOnlineMembers();
        log.info("获取群成员---------->" + JSONUtil.toJsonStr(members));

        return Result.succ(MapUtil.of("list", members));
    }

    /**
     * 获取聊天历史数据
     * @return
     */
    @GetMapping("/getGroupHistoryMsg")
    public Result getGroupHistoryMsg() {

        List<Object> messages = chatService.getGroupHistoryMsg(20);
        return Result.succ(messages);
    }
}
