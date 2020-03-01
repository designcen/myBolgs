package com.example.im.server;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.example.common.lang.Constant;
import com.example.im.handler.MsgHandler;
import com.example.im.handler.MsgHandlerFactory;
import com.example.service.ChatService;
import com.example.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.WsServerAioHandler;
import org.tio.websocket.server.handler.IWsMsgHandler;

import java.util.Map;
/**
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Slf4j
@Component
public class ImWsMsgHandler implements IWsMsgHandler {

    /**
     * 握手时走这个方法 {@link WsServerAioHandler#handler }
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {

        String username = httpRequest.getParam("username");
        String userId = httpRequest.getParam("userId");

        //绑定用户信息
        Tio.bindUser(channelContext, userId);

        log.info("{} - {} ------------> 握手啦握手啦", username, userId);
        return httpResponse;
    }

    /**
     * 握手成功之后操作
     * 1、通知上线
     * 2、
     * @param httpRequest
     * @param httpResponse
     * @param channelContext
     * @throws Exception
     */
    @Override
    public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) throws Exception {
        //绑定到群组，后面会有群发
        Tio.bindGroup(channelContext, Constant.IM_GROUP_NAME);

        //群聊人数
        int count = Tio.getAllChannelContexts(channelContext.groupContext).getObj().size();

        ChatService chatService = (ChatService) SpringUtil.getBean("chatService");
        String mess = chatService.buildSystemMess(Constant.IM_DEFAULT_GROUP_ID, channelContext.userid + "上线了");

        //用tio-websocket，服务器发送到客户端的Packet都是WsResponse
        WsResponse wsResponse = WsResponse.fromText(mess, "utf-8");
        //群发
        Tio.sendToGroup(channelContext.groupContext, Constant.IM_GROUP_NAME, wsResponse);

        // 添加成员
        chatService.putOnlineMember(httpRequest);
    }

    /**
     * 节消息（binaryType = arraybuffer）过来后会走这个方法
     * @param wsRequest
     * @param bytes
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        return null;
    }

    @Override
    public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) throws Exception {
        log.info("----> 用户 {} 退出群聊~", channelContext.userid);
        Tio.remove(channelContext, channelContext.userid + " - 退出群聊了~");

        //剔除成员
        ChatService chatService = (ChatService) SpringUtil.getBean("chatService");
        chatService.popOutlineMember(channelContext.userid);
        return null;
    }

    /**
     * 字符消息（binaryType = blob）过来后会走这个方法 {@link WsServerAioHandler#h}
     * @param wsRequest
     * @param s
     * @param channelContext
     * @return
     * @throws Exception
     */
    @Override
    public Object onText(WsRequest wsRequest, String s, ChannelContext channelContext) throws Exception {

        Map map = JSONUtil.toBean(s, Map.class);

        String type = MapUtil.getStr(map, "type");
        String data = MapUtil.getStr(map, "data");

        /**
         * 根据类型获取对应的处理器
         */
        MsgHandler msgHandler = MsgHandlerFactory.getMsgHandler(type);
        msgHandler.handler(data, wsRequest, channelContext);

        return null;
    }
}
