package com.example.im.server;

import lombok.extern.slf4j.Slf4j;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerStarter;

import java.io.IOException;

/**
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Slf4j
public class ImServerStarter {

    private ImWsMsgHandler imWsMsgHandler;
    private WsServerStarter wsServerStarter ;
    private ServerGroupContext serverGroupContext;
    public ImServerStarter(int imPort) throws IOException {

        imWsMsgHandler = new ImWsMsgHandler();
        wsServerStarter = new WsServerStarter(imPort, imWsMsgHandler);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setHeartbeatTimeout(1000 * 60);

    }

    public void start() throws IOException {
        this.wsServerStarter.start();
    }
}
