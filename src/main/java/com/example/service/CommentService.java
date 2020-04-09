package com.example.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.CommentVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页获取评论（从缓存中取）
     * @param page 分页
     * @param userId 用户id
     * @param postId 文章id
     * @param order 顺序
     * @return
     */
    IPage paging(Page page, Long userId, Long postId, String order);

    IPage<CommentVo> getLateComment(Page page,QueryWrapper<Comment> queryWrapper);

    void saveAndUpdate(Page page, Comment comment,String order);
}
