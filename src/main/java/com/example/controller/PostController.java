package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.service.CommentService;
import com.example.service.PostService;
import com.example.utils.RedisUtils;
import com.example.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

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
        IPage commentPage = commentService.paging(getPage(),null,id,"id");
        req.setAttribute("post",postVo);
        req.setAttribute("pageData",commentPage);
        return "post/view";
    }
}
