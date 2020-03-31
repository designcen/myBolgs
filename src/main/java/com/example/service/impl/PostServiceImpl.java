package com.example.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.example.mapper.PostMapper;
import com.example.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.hutool.core.date.DateUnit;
import com.example.utils.RedisUtils;
import com.example.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private PostMapper postMapper;
    @Override
    public void initIndexWeekRank() {
        // 缓存最近7天的文章评论数
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                        // ge是大于等于
                .ge("created", DateUtil.offsetDay(new Date(),-7).toJdkDate())
                .select("id,title,user_id,comment_count,view_count,created"));
        for (Post post  : last7DayPosts) {
            //定义键的格式 day_rank:yyyyMMdd
            String key = "day_rank:" + DateUtil.format(post.getCreated(),
                    DatePattern.PURE_DATE_PATTERN);
            // 设置有效期,目标时间与当前时间的差当前时间
            long between = DateUtil.between(new Date(),
                    post.getCreated(),DateUnit.DAY);
            // 换算成秒s
            long expireTime = (7 - between) * 24 * 60 * 60;
            // 缓存文章到zSet中，日期为集合名称，文章id为值（唯一），评论数量作为排行分数标准
            redisUtils.zSet(key,post.getId(),post.getCommentCount());
            // 设置有效期
            redisUtils.expire(key,expireTime);
            // 缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);
        }

        // 7天阅读相加
        this.zUnionAndStoreLast7DaysforLastWeekRank();
    }

    @Override
    @Cacheable(cacheNames = "cache_post", key = "'page_' + #page.current + '_' + #page.size " +
            "+ '_query_' +#userId  + '_' + #categoryId + '_' + #level  + '_' + #recommend  + '_' + #order")
    public IPage paging(Page page, Long userId, Long categoryId, Integer level, Boolean recommend, String order) {
        if (level == null) level = -1;
        QueryWrapper wrapper = new QueryWrapper<Post>()
                .eq(userId != null, "user_id", userId)
                .eq(categoryId != null && categoryId != 0, "category_id", categoryId)
                .ge(level >= 0,"level",level)
                .eq(recommend != null, "recommend", recommend)
                .orderByDesc(order);

        IPage<PostVo> pageDate = postMapper.selectPosts(page,wrapper);

        return pageDate;
    }

    @Override
    public PostVo selectOne(QueryWrapper wrapper) {
        return postMapper.selectOne(wrapper);
    }

    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId, boolean isIncr) {

        String dayRank = "day_rank:" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        // 文章阅读加一
        redisUtils.zIncrementScore(dayRank, postId, isIncr? 1: -1);
        this.hashCachePostIdAndTitle(this.getById(postId));
        // 重新union最近7天
        this.zUnionAndStoreLast7DaysforLastWeekRank();
    }

    @Override
    public void setViewCount(Post post) {
        // 从缓存中获取阅读量
        Integer viewCount = (Integer) redisUtils.hget("rank_post_" + post.getId(),"post:viewCount");
        if (viewCount != null) {
            post.setViewCount(viewCount + 1);
        }else{
            post.setViewCount(post.getViewCount() + 1);
        }
        // 设置新的阅读
        redisUtils.hset("rank_post_" + post.getId(),"post:viewCount",post.getViewCount());
    }

    /**
     * hash结构缓存文章标题和id
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {
        boolean isExist = redisUtils.hasKey("rank_post_"+post.getId());
        if (!isExist){
            long between = DateUtil.between(new Date(),
                    post.getCreated(),DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

            // 缓存文章基本信息（id、标题）（hash结构）
            redisUtils.hset("rank_post_" + post.getId(), "post:id",post.getId(),expireTime);
            redisUtils.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
        }
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    private void zUnionAndStoreLast7DaysforLastWeekRank() {
        String prifix = "day_rank:";
        List<String> keys = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
        for (int i = -7 ; i < 0 ;i++){
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            // 测试toJdkDate
//            Date date = DateUtil.offsetDay(new Date(), i);
            keys.add(prifix + DateUtil.format(date,DatePattern.PURE_DATE_PATTERN));
        }
        redisUtils.zUnionAndStore(key,keys,"last_week_rank");
    }
}
