package com.example.common.lang;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 公共常量
 */
@Data
@Component
public class Constant {


    @Value("${file.upload.dir}")
    private String uploadDir; // 用户本地文件上传的路径

    @Value("${file.upload.url}")
    private String uploadUrl; //项目中上传的路径

    public final static Long IM_DEFAULT_GROUP_ID = 999L;
    public static final Long IM_DEFAULT_USER_ID = 999L;

    // 评论cookie的键
    public final static String COMMENT_COOKIE = "comment_cookie";
    // 点赞cookie的键
    public final static String VOTE_COOKIE = "vote_cookie";

    // 群组名称
    public final static String IM_GROUP_NAME = "whatever_group_name";

    /*消息类型*/
    // 心跳
    public final static String IM_MESS_TYPE_PING = "pingMessage";
    // 聊天消息
    public final static String IM_MESS_TYPE_CHAT = "chatMessage";
    // 在线人数键
    public static final String IM_ONLINE_MEMBERS_KEY = "online_members_key";

    // 历史聊天数据键
    public static final String IM_GROUP_HISTROY_MSG_KEY = "group_histroy_msg_key";

    // 默认灰头像
    public static final String DEFAULT_GARY_AVATAR = "/res/images/default.jpg";

}
