package com.example.im.handler.impl;

import com.example.im.handler.MsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.websocket.common.WsRequest;
/**
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Slf4j
public class PingMsgHandler implements MsgHandler {

    @Override
    public void handler(String data, WsRequest wsRequest, ChannelContext channelContext) {

//        log.info("{} ------------>心跳中", channelContext.userid);

    }
}
