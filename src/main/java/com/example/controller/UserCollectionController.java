package com.example.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.entity.UserCollection;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/collection")
public class UserCollectionController extends BaseController {

    @PostMapping("/find")
    @ResponseBody
    public Result collectionFind(Long cid){
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id",cid)
                .eq("user_id",getProfileId()));
        return Result.succ(MapUtil.of("collection",count > 0));
    }

    @PostMapping("/add")
    @ResponseBody
    public Result collectionAdd(long cid){
        Post post = postService.getById(cid);
        Assert.isTrue(post != null,"该帖子已被删除");
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id",cid)
                .eq("user_id",getProfileId()));
        if (count > 0) {
            return Result.fail("你已经收藏");
        }
        UserCollection collection = new UserCollection();
        collection.setUserId(getProfileId());
        collection.setCreated(new Date());
        collection.setModified(new Date());
        userCollectionService.save(collection);
        return Result.succ(MapUtil.of("collection",true));
    }

    @PostMapping("/remove")
    @ResponseBody
    public Result collectionRemove(Long cid){
        Post post = postService.getById(Long.valueOf(cid));
        Assert.isTrue(post != null,"该帖子已经被删除");
        boolean hasRemove = userCollectionService.remove(new QueryWrapper<UserCollection>()
                .eq("post_id",cid)
                .eq("user_id",getProfileId()));
        return Result.succ(hasRemove);
    }
}
