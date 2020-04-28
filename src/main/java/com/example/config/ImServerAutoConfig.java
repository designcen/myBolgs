package com.example.config;

import com.example.im.handler.MsgHandlerFactory;
import com.example.im.server.ImServerStarter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.IOException;

/**
 * 初始化聊天配置
 * @author cenkang
 * @date 2020/2/29 - 20:27
 */
@Slf4j
@Data
@Configuration
@Order
public class ImServerAutoConfig {
    @Value("${im.server.port}")
    private Integer imPort;

    @Bean
    public ImServerStarter imServerStarter(){
        try {
            ImServerStarter imServerStarter = new ImServerStarter(imPort);
            imServerStarter.start();

            // 初始化消息处理器工程
            MsgHandlerFactory.init();

            log.info("---------> 群聊服务已启动!");
            return imServerStarter;

        } catch (IOException e) {
            log.error("im server 启动失败~~", e);
        }
        return null;
    }
}
