package com.example.im.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端发给前端的响应消息
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Data
public class ChatOutMess implements Serializable {

    // 消息类型
    private String emit;

    // 消息内容
    private Object mess;

    public ChatOutMess(){}

    public ChatOutMess (String emit, Object mess) {
        this.emit = emit;
        this.mess = mess;
    }

}
