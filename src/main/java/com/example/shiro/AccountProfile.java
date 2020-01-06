package com.example.shiro;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AccountProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;
    private String username;
    private String email;
    private Integer point;
    private String gender;
    private String avatar;
    private Integer postCount;
    private Integer commentCount;
    private Date lasted;
    private Integer vipLevel;

    public String getSex(){
        if(this.getGender() == null) return null;
        return this.getGender().equals("0") ? "男" : "女";
    }

}
