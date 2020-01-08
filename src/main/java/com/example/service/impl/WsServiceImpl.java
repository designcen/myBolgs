package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.UserMessage;
import com.example.service.UserMessageService;
import com.example.service.WsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author cenkang
 * @Date 2020/1/8 17:00
 */
@Slf4j
@Service
public class WsServiceImpl implements WsService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    UserMessageService userMessageService;
    /**
     * 订阅链接为/user/{userId}/messCount的用户能收到消息
     * /user为默认前缀
     *
     * @param userId
     * @param count
     */
    @Async // 新起一个线程来执行这个方法
    @Override
    public void sendMessCountToUser(Long userId, Integer count) {
        if (count == null) {
            count = userMessageService.count(new QueryWrapper<UserMessage>()
            .eq("status",0)
            .eq("to_user_id",userId));
        }
        // convertAndSendToUser方法会自动在前面添加前缀/user然后是userId，
        // 加上后面的后缀/messCount，即/user/{userId}/messCount
        // 需要发送消息的地方调用这个方法即可
        this.simpMessagingTemplate.convertAndSendToUser(userId.toString(),"/messCount",count);
        log.info("ws发送消息成功===============================>，数量：{}",userId,count);
    }
}
