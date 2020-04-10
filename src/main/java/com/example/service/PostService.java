package com.example.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vo.PostVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
public interface PostService extends IService<Post> {
    /**
     * 初始化首页的周评论排行榜 启动时候将数据存入reids
     */
    public  void initIndexWeekRank();

    IPage<PostVo> paging(Page page,Long userId, Long categoryId,Integer level, Boolean recommend, String order);

    PostVo selectOne(QueryWrapper wrapper);

    void incrZsetValueAndUnionForLastWeekRank(Long postId, boolean isIncr);

    /**
     * 设置访问量，+1、-1添加到redis中等待定时任务时再同步到数据库中
     * @param post
     */
    void setViewCount(Post post);
}
