package com.example.schedules;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.service.PostService;
import com.example.utils.RedisUtils;
import javafx.geometry.Pos;
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
     * 阅读数量同步任务
     * 每天2点同步
     */
    @Scheduled(cron = "0 0 2 * * ?")
    // @Scheduled(cron = "0 0/1 * * * *") // 一分钟（测试用）
    public void postViewCountSync(){
        Set<String> keys = redisTemplate.keys("rank_post_*");
        List<String> ids = new ArrayList<>();
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
