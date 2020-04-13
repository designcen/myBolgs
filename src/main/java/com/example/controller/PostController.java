package com.example.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.lang.Constant;
import com.example.common.lang.Result;
import com.example.config.RabbitMqConfig;
import com.example.entity.*;
import com.example.search.common.PostMqIndexMessage;
import com.example.utils.CommonUtils;
import com.example.vo.PostVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 博客帖子控制器
 *
 * @author cenkang
 * @since 2019-12-26
 */
// 把@RestController替换为@Controller注解
// @RestController注解表示返回的内容都是HTTP Content不会被模版引擎处理的
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 查看帖子
     * @param id 帖子id
     * @return
     */
    @RequestMapping("/{id:\\d*}")
    public String view(@PathVariable Long id) {
        QueryWrapper wrapper = new QueryWrapper<Post>().eq(id != null, "p.id", id);
        PostVo postVo = postService.selectOne(wrapper);
        Assert.notNull(postVo, "文章已被删除！");
        // 获取该请求的cookie,判断该请求是否读过该文章，若没有，则添加访问量，并且添加该文章的cookie
        Cookie cookie = CommonUtils.getCookie(req, Constant.COMMENT_COOKIE + id);
        if (cookie == null) {
            // redis中访问量+1
            postService.setViewCount(postVo);
            // 添加含该文章标记的cookie
            CommonUtils.addcookie(resp, Constant.COMMENT_COOKIE + id, "true", null, 0);
        }
        // 从redis中获取浏览数量
        int viewCount = postService.getViewCount(postVo);
        if (viewCount != 0) {
            postVo.setViewCount(viewCount);
        }
        // 分页获取评论
        IPage commentPage = commentService.paging(getPage(), null, id, "id");
        req.setAttribute("post", postVo);
        req.setAttribute("pageData", commentPage);
        return "post/view";
    }

    /**
     * 删除帖子
     *
     * @param id 帖子id
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    @ResponseBody
    public Result delete(Long id) throws JsonProcessingException {
        Post post = postService.getById(id);
        Assert.notNull(post, "该帖子已被删除");
        Assert.isTrue(post.getUserId() == getProfileId(), "无权限删除此文章！");
        postService.removeById(id);
        // 删除相关消息
        userMessageService.removeByMap(MapUtil.of("post_id", id));
        // 删除收藏
        userCollectionService.removeByMap(MapUtil.of("post_id", id));
        // 删除文章（帖子）相关的评论
        commentService.removeByMap(MapUtil.of("post_id", id));
        //发送异步消息更新ES
        amqpTemplate.convertAndSend(RabbitMqConfig.ES_EXCHANGE, RabbitMqConfig.ES_BIND_KEY,
                objectMapper.writeValueAsString(new PostMqIndexMessage(post.getId(), PostMqIndexMessage.REMOVE)));
        return Result.succ("删除成功", null, "/center");
    }

    @GetMapping("/edit")
    public String edit() {
        String id = req.getParameter("id");
        Post post = new Post();
        if (!StringUtils.isEmpty(id)) {
            post = postService.getById(Long.valueOf(id));
            Assert.notNull(post, "文章已被删除！");
            Long profileId = getProfileId();
            Assert.isTrue(post.getUserId() == getProfileId(), "无权限编辑此文章！");
        }
        List<Category> categories = categoryService.list(new QueryWrapper<Category>()
                .orderByDesc("order_num"));
        req.setAttribute("post", post);
        req.setAttribute("categories", categories);
        return "post/edit";

    }

    /**
     * 编辑文章（帖子）
     * @param id
     * @return
     */
    @PostMapping("/getDa")
    @ResponseBody
    public Result getDa(Long id) {
        Assert.isNull(id, "该文章不存在！");
        Post post = postService.getById(id);
        Assert.notNull(post, "文章已被删除！");
        return Result.succ(null,post,"post/edit");
    }

    /**
     * 更新文章（帖子）
     * @param id
     * @return
     */
    @PostMapping("/updateDa")
    @ResponseBody
    public Result updateDa(Long id,String content) {
        Assert.isNull(id, "文章id不能为空");
        Post post = postService.getById(id);
        Assert.notNull(post, "文章已被删除！");
        Assert.isTrue(post.getUserId() == getProfileId(), "无权限编辑此文章！");
        postService.updateById(post);
        return Result.succ(null);
    }

    /**
     * 发布新帖
     * @param post
     * @param bindingResult
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/publish")
    @Transactional
    @ResponseBody
    public Result submit(@Valid Post post, BindingResult bindingResult) throws JsonProcessingException {
        // bindingResult.hasErrors()是判断提交的post是否符合验证逻辑的
        if (bindingResult.hasErrors()) {
            return Result.fail(bindingResult.getFieldError().getDefaultMessage());
        }
        String type;
        if (post.getId() == null) {
            post.setUserId(getProfileId());
            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(null);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            type = PostMqIndexMessage.CREATE;
        } else {
            Post tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId() == getProfileId(), "无权限编辑此文章！");
            type = PostMqIndexMessage.UPDATE;
        }
        postService.saveOrUpdate(post);
//        postService.setRedisPostRank(post);
        // 发送异步消息更新ES
        amqpTemplate.convertAndSend(RabbitMqConfig.ES_EXCHANGE, RabbitMqConfig.ES_BIND_KEY,
                objectMapper.writeValueAsString(new PostMqIndexMessage(post.getId(), type)));
        return Result.succ("发布成功", null, "/post/" + post.getId());
    }

    @GetMapping("/add")
    public String add(){
        List<Category> categories = categoryService.list();
        req.setAttribute("categories",categories);
        return "post/edit";
    }
}
