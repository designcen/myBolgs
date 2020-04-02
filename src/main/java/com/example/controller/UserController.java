package com.example.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.UserCollection;
import com.example.shiro.AccountProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  用户基本信息
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    /**
     * 跳转用户主页
     * @param id 用户id
     * @return
     */
    @RequestMapping("/{id:\\d*}")
    public String home(@PathVariable Long id){
        User user = userService.getById(id);
        user.setPassword(null);
        // 30天文章的内容
        Date date30Before = DateUtil.offsetDay(new Date(),-30).toJdkDate();
        List<Post> posts = postService.list(new QueryWrapper<Post>()
        .eq("user_id",id)
        .ge("created",date30Before)
        .orderByDesc("created"));

        req.setAttribute("user",user);
        req.setAttribute("posts",posts);
        return "user/home";
    }

    /**
     * 跳转到用户中心并展示当前用户所有的发帖（文章）
     * @return
     */
    @RequestMapping("/index")
    public String index(){
        // 获取当前shiro中的用户
        AccountProfile suser = getProfile();
        // 获取分页
        Page page = getPage();
        // 分页获取当前用户的所有文章
        IPage postAll = postService.paging(getPage(), suser.getId(), null, suser.getVipLevel(), null, "created");
        // 获取收藏数量
        int collectionCount = userCollectionService.count(new QueryWrapper<UserCollection>().eq("user_id",getProfileId()));
        // 获取发帖（文章）数量
        int postCount = postService.count(new QueryWrapper<Post>().eq("user_id",getProfileId()));

        req.setAttribute("pageDate",postAll);
        req.setAttribute("collectionCount",collectionCount);
        req.setAttribute("postCount",postCount);
        return "user/index";
    }

}
