package com.example.templates;

import com.example.common.template.DirectiveHandler;
import com.example.common.template.TemplateDirective;
import com.example.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import java.util.*;

/**
 * @author cenkang
 * @date 2020/1/1 - 20:42
 */
@Component
public class HotsTemplate extends TemplateDirective {

    @Autowired
    RedisUtils redisUtils;
    @Override
    public String getName() {
        return "hots";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtils.getZSetRank("last_week_rank",0,6);
        List<Map<String,Object>> hotPosts = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {
          Map<String,Object> map = new HashMap<>();
          map.put("comment_count",typedTuple.getScore());
          map.put("id",redisUtils.hget("rank_post_"+typedTuple.getValue(),"post:title"));
          map.put("title",redisUtils.hget("rank_post_"+typedTuple.getValue(),"post:title"));
          hotPosts.add(map);
        }
        handler.put(RESULTS,hotPosts).render();
    }
}
