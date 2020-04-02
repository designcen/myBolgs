package com.example.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.shiro.AccountProfile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping("/index")
    public String index(){
        // 获取当前shiro中的用户
        AccountProfile suser = getProfile();
        // 获取分页
        Page page = getPage();
        // 分页获取当前用户的所有文章
        IPage postAll = postService.paging(getPage(), suser.getId(), null, suser.getVipLevel(), null, "created");
        req.setAttribute("pageDate",postAll);

        return "user/index";
    }

}
