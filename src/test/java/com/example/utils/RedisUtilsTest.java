package com.example.utils;

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

}
