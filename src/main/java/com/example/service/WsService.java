package com.example.service;

/**
 * 发送消息数量给前端的方法
 * @author cenkang
 * @Date 2020/1/8 16:59
 */
public interface WsService {
    void sendMessCountToUser(Long userId,Integer count);
}
