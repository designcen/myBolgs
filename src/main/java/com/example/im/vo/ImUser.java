package com.example.im.vo;

import lombok.Data;

import java.io.Serializable;
/**
 * @author cenkang
 * @date 2020/2/29 - 20:33
 */
@Data
public class ImUser implements Serializable {

    public final static String ONLINE_STATUS = "online";
    public final static String HIDE_STATUS = "hide";


    private Long id;
    private String username;
    private String status;     //在线状态 online：在线、hide：隐身
    private String sign;       //我的签名
    private String avatar;     //我的头像

    private Boolean mine;
    private String content;
}
