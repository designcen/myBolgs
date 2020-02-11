package com.example.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.service.PostService;
import com.example.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 定时器功能
 * @author cenkang
 * @Date 2020/1/8 17:47
 */
@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    PostService postService;
    /**
     * 阅读数量同步任务，将redis的阅读数量同步到数据库中
     * 每天2点同步
     */
    @Scheduled(cron = "0 0 2 * * ?")
    // @Scheduled(cron = "0 0/1 * * * *") // 一分钟（测试用）
    public void postViewCountSync(){
        /**
         * 获取所有需要同步阅读的列表，这里用了keys命令，
         * 实际上当redis的缓存越来越大的时候，是不能再使用这keys命令的，因为keys命令会检索所有的key，是个耗时的过程，
         * 而redis又是个单线程的中间件，会影响其他命令的执行。所以理论上需要用scan命令。考虑到这里博客只是个简单业务，
         * redis不会很大，所以就直接用了keys命令，后期需要优化。
         *
         * 获取到列表后，就是获取所有的实体，然后批量更新阅读量。
         */
        Set<String> keys = redisTemplate.keys("rank_post_*");
        List<String> ids = new ArrayList<>();
        // 获取每篇文章的id
        for (String key  : keys) {
            String postId = key.substring("rank_post_".length());
            if (redisUtils.hHasKey("rank_post_" + postId,"post:viewCount")) {
                ids.add(postId);
            }
        }
        if (ids.isEmpty()) return;
        List<Post> posts = postService.list(new QueryWrapper<Post>().in("id",ids));
        Iterator<Post> iterator = posts.iterator();
        List<String> syncKeys = new ArrayList<>();

        // 将redis中的阅读数量同步到数据库中
        while (iterator.hasNext()){
            Post post = iterator.next();
            Object count = redisUtils.hget("rank_post_" + post.getId(),"post:viewCount");
            if (count != null) {
                post.setViewCount(Integer.valueOf(count.toString()));
                syncKeys.add("rank_post_" + post.getId());
            }else{
                // 不需要同步的
            }
        }

        if (posts.isEmpty()) return;
        boolean isSuccess = postService.updateBatchById(posts);
        if (isSuccess) {
            for (Post post  : posts) {
              // 删除缓存中的阅读数量，防止重复同步（根据实际情况来）
                redisUtils.hdel("rank_post_" + post.getId(),"post:viewCount");
            }
        }
        log.info("同步文章阅读成功===================>{}",syncKeys);
    }
}
