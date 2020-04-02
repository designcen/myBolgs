package com.example.vo;

import com.example.entity.Post;
import lombok.Data;

import java.util.Date;

/**
 * @author cenkang
 * @date 2020/4/2 - 23:18
 */
@Data
public class CollectionVo extends Post{
    /**
     * 帖子（文章）用户的id
     */
    private Integer postUserId;
    /**
     * 收藏帖子（文章）创建时间
     */
    private Date collectionCreated;
    /**
     * 收藏帖子（文章）修改时间
     */
    private Date collectionModified;
}
