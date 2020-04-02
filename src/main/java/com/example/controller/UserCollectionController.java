package com.example.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.entity.UserCollection;
import com.example.vo.CollectionVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * <p>
 *  我收藏的帖子（文章）
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Controller
@RequestMapping("/collection")
public class UserCollectionController extends BaseController {

    /**
     * 查询我收藏的帖子（文章）
     * @param
     * @return
     */
    @RequestMapping("/find")
    public String collectionFind(){
        // 获取分页
        Page page = getPage();
        // 获取当前用户收藏的文章
        IPage<CollectionVo> collectionList = userCollectionService.paging(page,new QueryWrapper<UserCollection>().eq("u.user_id",getProfileId()));
        // 获取收藏数量
        int collectionCount = userCollectionService.count(new QueryWrapper<UserCollection>().eq("user_id",getProfileId()));
        // 获取发帖（文章）数量
        int postCount = postService.count(new QueryWrapper<Post>().eq("user_id",getProfileId()));
        req.setAttribute("pageData",collectionList);
        req.setAttribute("collectionCount",collectionCount);
        req.setAttribute("postCount",postCount);
        return "user/find";
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
