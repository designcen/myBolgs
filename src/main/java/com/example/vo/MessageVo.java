package com.example.vo;

import com.example.entity.UserMessage;
import com.example.mapper.UserMapper;

/**
 * @author cenkang
 * @date 2020/1/7 - 22:32
 */
public class MessageVo extends UserMessage {
    private String toUserName;
    private String fromUserName;
    private String postTitle;
    private String commentContent;
}
