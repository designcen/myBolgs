package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 双工异步通信功能，
 * @author cenkang
 * @Date 2020/1/8 16:43
 */
@Configuration
@EnableWebSocketMessageBroker // 表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /***
     * 注册Stomp的端点
     * addEndpoint：添加STOMP协议的端点。提供WebSocket或SockJS客户端访问的地址
     * withSockJS：使用SockJS协议
     * @param stompEndpointRegistry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry){
        // 注册一个端点叫websocket
        // 前端就能通过这个链接连接到服务器实现双工通讯了
        // withSockJS()意思是使用SockJs协议
        // SockJs是解决浏览器不支持ws的情况,tompjs是简化文本传输的格式
        stompEndpointRegistry.addEndpoint("/websocket").withSockJS();
    }
    /**
     * 配置消息代理
     * 启动Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
     * @param messageBrokerRegistry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry){
        // 推送消息前缀
        messageBrokerRegistry.enableSimpleBroker("/user/","/topic");
        messageBrokerRegistry.setApplicationDestinationPrefixes("/app");
    }
}
