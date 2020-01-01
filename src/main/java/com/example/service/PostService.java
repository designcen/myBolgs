package com.example.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

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

    IPage paging(Page page, Long categoryId, String order);
}
