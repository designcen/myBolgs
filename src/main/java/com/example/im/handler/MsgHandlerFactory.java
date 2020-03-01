package com.example.im.handler;

import com.example.common.lang.Constant;
import com.example.im.handler.impl.ChatMsgHandler;
import com.example.im.handler.impl.PingMsgHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、消息处理器初始化工程
 * 2、根据类型获取消息处理器
 * @author cenkang
 * @date 2020/2/29 - 20:45
 */
public class MsgHandlerFactory {
    private static boolean isInit = false;
    private static Map<String, MsgHandler> handlerMap = new HashMap<>();

    /**
     * 得预先初始化消息处理器
     */
    public static void init(){
        if(isInit){ return; }

        handlerMap.put(Constant.IM_MESS_TYPE_CHAT, new ChatMsgHandler());
        handlerMap.put(Constant.IM_MESS_TYPE_PING, new PingMsgHandler());

        isInit = true;
    }

    public static MsgHandler getMsgHandler(String type) {
        return handlerMap.get(type);
    }
}
