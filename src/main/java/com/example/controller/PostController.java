package com.example.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.entity.UserCollection;
import com.example.vo.PostVo;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("")
public class PostController extends BaseController {

    /**
     *  导航分类
     * @param id 分类id
     * @return
     */
    @GetMapping("/category/{id:\\d*}")
    public String category(@PathVariable Long id){
        Page page = getPage();
        IPage<PostVo> pageData = postService.paging(page,null,id,null,null,"created");
        req.setAttribute("pageData",pageData);
        // currentCategoryId是为了回显我当前选择的栏目
        req.setAttribute("currentCategoryId",id);
       return "post/category";
    }

    @RequestMapping("/post/{id:\\d*}")
    public String view(@PathVariable Long id){
        QueryWrapper wrapper = new QueryWrapper<Post>().eq(id != null,"p.id",id);
        PostVo postVo = postService.selectOne(wrapper);
        Assert.isNull(postVo, "文章已被删除！");
        postService.setViewCount(postVo);
        IPage commentPage = commentService.paging(getPage(),null,id,"id");
        req.setAttribute("post",postVo);
        req.setAttribute("pageData",commentPage);
        return "post/view";
    }

    @PostMapping("/collection/find/")
    @ResponseBody
    public Result collectionFind(Long cid){
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
        .eq("post_id",cid)
        .eq("user_id",getProfileId()));
        return Result.succ(MapUtil.of("collection",count > 0));
    }

    @PostMapping("/collection/add/")
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

    @PostMapping("/collection/remove/")
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
