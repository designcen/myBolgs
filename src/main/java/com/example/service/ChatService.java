package com.example.service;

import com.example.im.vo.ImMess;
import com.example.im.vo.ImUser;
import org.tio.http.common.HttpRequest;

import java.util.List;
import java.util.Set;

public interface ChatService {
    /**
     * 获取当前用户信息
     * @return
     */
    ImUser getCurrentImUser();

    /**
     * 创建系统消息
     * @param imDefaultGroupId 系统默认分组id
     * @param s 消息内容
     * @return
     */
    String buildSystemMess(Long imDefaultGroupId, String s);

    /**
     * 获取所有在线人信息
     * @return
     */
    Set<Object> findAllOnlineMembers();

    /**
     * 添加在线成员
     * @param httpRequest
     */
    void putOnlineMember(HttpRequest httpRequest);

    /**
     * 剔除下线成员
     * @param userid
     */
    void popOutlineMember(String userid);

    /**
     * 存放历史聊天消息
     * @param responseMess
     */
    void setGroupHistoryMsg(ImMess responseMess);

    /**
     * 获取历史聊天消息
     * @param count 聊天条数
     * @return
     */
    List<Object> getGroupHistoryMsg(int count);
}
