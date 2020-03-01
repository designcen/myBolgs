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

    private String emit;

    private Object mess;

    public ChatOutMess(){}

    public ChatOutMess (String emit, Object mess) {
        this.emit = emit;
        this.mess = mess;
    }

}
