package com.example.vo;

import com.example.entity.Comment;
import lombok.Data;

@Data
public class CommentVo extends Comment {

    /**
     * 文章作者id
     */
    private Long authorId;
    /**
     * 文章作者姓名
     */
    private String authorName;
    /**
     * 文章作者头像
     */
    private String authorAvatar;
    /**
     * 文章作者vip等级
     */
    private int authorVip;
    /**
     * 文章标题
     */
    private String title;
}
