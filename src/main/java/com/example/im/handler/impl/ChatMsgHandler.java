package com.example.im.handler.impl;

import cn.hutool.json.JSONUtil;
import com.example.common.lang.Constant;
import com.example.im.handler.MsgHandler;
import com.example.im.handler.filter.ChannelContextFilterImpl;
import com.example.im.message.ChatInMess;
import com.example.im.message.ChatOutMess;
import com.example.im.vo.ImMess;
import com.example.im.vo.ImTo;
import com.example.im.vo.ImUser;
import com.example.service.ChatService;
import com.example.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.ChannelContextFilter;
import org.tio.core.Tio;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;

import java.util.Date;
/**
 * 消息发送处理
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Slf4j
@Component
public class ChatMsgHandler implements MsgHandler {

    @Override
    public void handler(String data, WsRequest wsRequest, ChannelContext channelContext) {
        ChatInMess chatMess = JSONUtil.toBean(data, ChatInMess.class);

        log.info("--------------> {}", chatMess.toString());

        ImUser mine = chatMess.getMine();
        ImTo to = chatMess.getTo();
        ImMess responseMess = new ImMess();
        responseMess.setContent(mine.getContent());
        responseMess.setAvatar(mine.getAvatar());
        responseMess.setMine(false);//是否我自己发的信息(自己不需要发送给自己)
        responseMess.setUsername(mine.getUsername());
        responseMess.setFromid(mine.getId());
        responseMess.setTimestamp(new Date());
        responseMess.setType(to.getType());
        responseMess.setId(Constant.IM_DEFAULT_GROUP_ID);//群组的id

        ChatOutMess chatOutMess = new ChatOutMess(Constant.IM_MESS_TYPE_CHAT, responseMess);

        String responseData = JSONUtil.toJsonStr(chatOutMess);
        log.info("群发消息 =========> {}", responseData);

        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(responseData, "utf-8");
        // 过滤自己发送给自己，因为在layui框架中已经将消息发送给自己一遍了
        ChannelContextFilter filter = new ChannelContextFilterImpl();
        ((ChannelContextFilterImpl) filter).setCurrentContext(channelContext);

        //群发
        Tio.sendToGroup(channelContext.groupContext, Constant.IM_GROUP_NAME, wsResponse, filter);

        //保存群聊信息
        ChatService chatService = (ChatService) SpringUtil.getBean("chatService");
        chatService.setGroupHistoryMsg(responseMess);

    }
}
