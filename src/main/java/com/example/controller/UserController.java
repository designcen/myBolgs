package com.example.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Post;
import com.example.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
public class UserController extends BaseController {

    @RequestMapping("/user/{id:\\d*}")
    public String home(@PathVariable Long id){
        User user = userService.getById(id);
        user.setPassword(null);
        // 30天内容的文章
        Date date30Before = DateUtil.offsetDay(new Date(),-30).toJdkDate();
        List<Post> posts = postService.list(new QueryWrapper<Post>()
        .eq("user_id",id)
        .ge("created",date30Before)
        .orderByDesc("created"));
        req.setAttribute("user",user);
        req.setAttribute("posts",posts);
        return "user/home";
    }
}
