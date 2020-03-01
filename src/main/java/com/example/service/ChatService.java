package com.example.service;

import com.example.im.vo.ImMess;
import com.example.im.vo.ImUser;
import org.tio.http.common.HttpRequest;

import java.util.List;
import java.util.Set;

public interface ChatService {

    ImUser getCurrentImUser();

    String buildSystemMess(Long imDefaultGroupId, String s);

    Set<Object> findAllOnlineMembers();

    void putOnlineMember(HttpRequest httpRequest);

    void popOutlineMember(String userid);

    void setGroupHistoryMsg(ImMess responseMess);

    List<Object> getGroupHistoryMsg(int count);
}
