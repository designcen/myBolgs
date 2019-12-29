package com.example.vo;

import com.example.entity.Post;
import lombok.Data;

import java.io.Serializable;

/**
 * @author cenkang
 * @date 2019/12/29 - 13:35
 */
@Data
public class PostVo extends Post implements Serializable{
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private int authorVip;

    private String categoryName;
}
