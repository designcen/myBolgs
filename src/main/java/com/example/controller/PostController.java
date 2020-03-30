package com.example.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.lang.Result;
import com.example.config.RabbitMqConfig;
import com.example.entity.*;
import com.example.search.common.PostMqIndexMessage;
import com.example.vo.PostVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 *  博客帖子控制器
 * @author cenkang
 * @since 2019-12-26
 */
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
    public String view(@PathVariable Long id){
        QueryWrapper wrapper = new QueryWrapper<Post>().eq(id != null,"p.id",id);
        PostVo postVo = postService.selectOne(wrapper);
        Assert.notNull(postVo, "文章已被删除！");
        postService.setViewCount(postVo);
        IPage commentPage = commentService.paging(getPage(),null,id,"id");
        req.setAttribute("post",postVo);
        req.setAttribute("pageData",commentPage);
        return "post/view";
    }

    /**
     * 删除帖子
     * @param id 帖子id
     * @return
     */
    @PostMapping("/delete")
    @Transactional
    @ResponseBody
    public Result delete(Long id) throws JsonProcessingException {
        Post post = postService.getById(id);
        Assert.notNull(post,"该帖子已被删除");
        Assert.isTrue(post.getUserId() == getProfileId(),"无权限删除此文章！");
        postService.removeById(id);
        // 删除相关消息，收藏等
        userMessageService.removeByMap(MapUtil.of("post_id", id));
        userCollectionService.removeByMap(MapUtil.of("post_id",id));

        //发送异步消息更新ES
        amqpTemplate.convertAndSend(RabbitMqConfig.ES_EXCHANGE, RabbitMqConfig.ES_BIND_KEY,
                objectMapper.writeValueAsString(new PostMqIndexMessage(post.getId(), PostMqIndexMessage.REMOVE)));
        return Result.succ("删除成功",null,"/center");
    }

    /**
     * 评论帖子
     * @param pid 帖子id
     * @param parentId
     * @param content 评论内容
     * @return
     */
    @PostMapping("/reply")
    @Transactional
    @ResponseBody
    public Result reply(Long pid,Long parentId,String content) {
        Assert.notNull(pid, "找不到对应文章！");
        Assert.hasLength(content, "评论内容不能为空！");
        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该文章已被删除");
        // 保存评论
        Comment comment = new Comment();
        comment.setPostId(pid);
        comment.setContent(content);
        comment.setUserId(getProfileId());
        comment.setCreated(new Date());
        comment.setModified(new Date());
        comment.setLevel(0);
        comment.setVoteDown(0);
        comment.setVoteUp(0);
        commentService.save(comment);
        // 评论数量加一
        post.setCommentCount(post.getCommentCount() + 1);
        postService.saveOrUpdate(post);
        // 更新首页排版评论数量
        postService.incrZsetValueAndUnionForLastWeekRank(comment.getPostId(), true);
        // 自己评论自己不需要通知
        if (post.getUserId() != getProfileId()) {
            // 通知作者
            UserMessage message = new UserMessage();
            message.setPostId(pid);
            message.setCommentId(comment.getId());
            message.setFromUserId(getProfileId());
            message.setToUserId(post.getUserId());
            message.setType(1);
            message.setContent(comment.getContent());
            message.setCreated(new Date());
            userMessageService.save(message);
            // webSocket向浏览器发送消息
            wsService.sendMessCountToUser(message.getToUserId(),null);
        }
        // 通知@的人
        if (content.startsWith("@")) {
            // 提取用户名称
            String username = content.substring(1, content.indexOf(" "));
            System.out.println(username);
            QueryWrapper<User> wrapper = new QueryWrapper<User>().eq("username", username);
            User user = userService.getOne(wrapper);
            if (user != null) {
                UserMessage message2 = new UserMessage();
                message2.setPostId(pid);
                message2.setCommentId(comment.getId());
                message2.setFromUserId(getProfileId());
                message2.setToUserId(user.getId());
                message2.setType(3);
                message2.setContent(comment.getContent());
                message2.setCreated(new Date());
                userMessageService.save(message2);
                wsService.sendMessCountToUser(message2.getToUserId(),null);
            }
        }
        return Result.succ("", null);
    }

    @PostMapping("/jieda-delete")
    @Transactional
    @ResponseBody
    public Result reply(Long id){
        Assert.notNull(id,"评论id不能为空！");
        Comment comment = commentService.getById(id);
        Assert.notNull(comment,"找不到对应评论！");
        if (comment.getUserId() != getProfileId()) {
            return Result.fail(("不是你发表的评论！"));
        }
        commentService.removeById(id);
        // 评论数量减一
        Post post = postService.getById(comment.getPostId());
        post.setCommentCount(post.getCommentCount() - 1);
        postService.saveOrUpdate(post);

        //评论数量减一
        postService.incrZsetValueAndUnionForLastWeekRank(comment.getPostId(), false);

        return Result.succ(null);
    }
    @GetMapping("/edit")
    public String edit(){
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
        req.setAttribute("post",post);
        req.setAttribute("categories",categories);
        return "post/edit";

    }

    @PostMapping("/submit")
    @Transactional
    @ResponseBody
    public Result submit(@Valid Post post, BindingResult bindingResult, String vercode) throws JsonProcessingException {
        // 后期改正，此处应该是页面验证码
        if (!"2".equals(vercode)) {
            return Result.fail("验证码错误");
        }
        // bindingResult.hasErrors()是判断提交的post是否符合验证逻辑的
        if (bindingResult.hasErrors()) {
            return  Result.fail(bindingResult.getFieldError().getDefaultMessage());
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
        }else{
            Post tempPost = postService.getById(post.getId());
            Assert.isTrue(tempPost.getUserId() == getProfileId(),"无权限编辑此文章！");
            type = PostMqIndexMessage.UPDATE;
        }
        postService.saveOrUpdate(post);

        //发送异步消息更新ES
        amqpTemplate.convertAndSend(RabbitMqConfig.ES_EXCHANGE, RabbitMqConfig.ES_BIND_KEY,
                objectMapper.writeValueAsString(new PostMqIndexMessage(post.getId(), type)));
        return Result.succ("发布成功",null,"/post/" + post.getId());
    }
}
