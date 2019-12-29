package com.example.controller;


import com.example.common.lang.Result;
import com.example.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.controller.BaseController;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;

    @GetMapping("/post/hots")
    @ResponseBody
    public Result hotPost(){
        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtils.getZSetRank("last_week_rank",0,6);
        List<Map<String,Object>> hotPosts = new ArrayList<>();
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {
          Map<String,Object> map = new HashMap<>();
          map.put("common_count",typedTuple.getScore());
          map.put("id",redisUtils.hget("rank_post_"+typedTuple.getValue(),"post:id"));
          map.put("title",redisUtils.hget("rank_post_"+typedTuple.getValue(),"post:title"));
          hotPosts.add(map);
        }
        return Result.succ(hotPosts);
    }

}
