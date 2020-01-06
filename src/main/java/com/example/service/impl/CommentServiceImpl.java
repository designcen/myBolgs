package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Comment;
import com.example.mapper.CommentMapper;
import com.example.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Override
    @Cacheable(cacheNames = "cache_comment",key = "'page_'+#page.current+'_'+#page.size+'_query_'+#userId+'_'+#postId+'_'+#order")
    public IPage paging(Page page, Long userId, Long postId, String order) {
        QueryWrapper wrapper = new QueryWrapper<Comment>()
                .eq(userId != null,"c.user_id",userId)
                .eq(postId != null,"c.post_id",postId);
        IPage<CommentVo> pageData = commentMapper.selectComments(page,wrapper);
        return pageData;
    }
}
