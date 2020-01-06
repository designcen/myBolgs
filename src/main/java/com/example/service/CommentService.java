package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface CommentService extends IService<Comment> {

    IPage paging(Page page, Long userId, Long postId, String order);
}
