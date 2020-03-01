package com.example.im.message;

import com.example.im.vo.ImTo;
import com.example.im.vo.ImUser;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端发送过来的消息
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Data
public class ChatInMess implements Serializable {

    private ImUser mine;
    private ImTo to;
}
