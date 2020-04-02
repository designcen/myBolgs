package com.example.entity;

import com.example.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UserCollection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 当前用户的id
     */
    private Long userId;

    /**
     * 帖子（文章）id
     */
    private Long postId;

    /**
     * 帖子（文章）作者id
     */
    private Long postUserId;


}
