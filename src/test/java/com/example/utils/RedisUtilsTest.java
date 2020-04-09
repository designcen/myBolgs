package com.example.utils;

import com.example.entity.Comment;
import com.example.entity.UserAction;
import com.example.entity.UserCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;

/**
 * @author cenkang
 * @date 2020/1/1 - 22:24
 */
@SpringBootTest
public class RedisUtilsTest {
    @Autowired
    RedisUtils redisUtils;
    @Test
    public void getvalue(){
        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtils.getZSetRank("last_week_rank",0,6);
        List<Map<String,Object>> hotPosts = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {
            Map<String,Object> map = new HashMap<>();
            map.put("comment_count",typedTuple.getScore());
            map.put("id",redisUtils.hget("rank_post_"+typedTuple.getValue(),"post:title"));
            hotPosts.add(map);
        }
    }



    @Test
    public void copyProperties(){
        Object u = new Comment();
        Comment comment = new Comment();
        comment.setContent("1");
        comment.setLevel(2);
        comment.setPostId(3L);
        comment.setUserId(4L);
        comment.setVoteDown(5);
        comment.setParentId(6L);
        CommonUtils.copyProperties(u,comment);
        System.out.println(u);
    }
}
