package com.example.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.lang.Result;
import com.example.entity.*;
import com.example.vo.PostVo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  帖子
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {

    @RequestMapping("/{id:\\d*}")
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

    @PostMapping("/delete")
    @Transactional
    @ResponseBody
    public Result delete(Long id){
        Post post = postService.getById(id);
        org.springframework.util.Assert.notNull(post,"该帖子已被删除");
        org.springframework.util.Assert.isTrue(post.getUserId() == getProfileId(),"无权限删除此文章！");
        postService.removeById(id);
        // 删除相关消息，收藏等
        userMessageService.removeByMap(MapUtil.of("post_id", id));
        userCollectionService.removeByMap(MapUtil.of("post_id",id));
        return Result.succ("删除成功",null,"/center");
    }

    @PostMapping("/reply")
    @Transactional
    @ResponseBody
    public Result reply(Long pid,Long parentId,String content) {
        org.springframework.util.Assert.notNull(pid, "找不到对应文章！");
        org.springframework.util.Assert.hasLength(content, "评论内容不能为空！");
        Post post = postService.getById(pid);
        org.springframework.util.Assert.isTrue(post != null, "该文章已被删除");
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
        //更新首页排版版的评论数量
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
        org.springframework.util.Assert.notNull(id,"评论id不能为空！");
        Comment comment = commentService.getById(id);
        org.springframework.util.Assert.notNull(comment,"找不到对应评论！");
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
            org.springframework.util.Assert.notNull(post, "文章已被删除！");
            Long profileId = getProfileId();
            org.springframework.util.Assert.isTrue(post.getUserId() == getProfileId(), "无权限编辑此文章！");
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
    public Result submit(@Valid Post post, BindingResult bindingResult, String vercode){
        // 后期改正，此处应该是页面验证码
        if (!"2".equals(vercode)) {
            return Result.fail("验证码错误");
        }
        // bindingResult.hasErrors()是判断提交的post是否符合验证逻辑的
        if (bindingResult.hasErrors()) {
            return  Result.fail(bindingResult.getFieldError().getDefaultMessage());
        }
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
        }else{
            Post tempPost = postService.getById(post.getId());
            org.springframework.util.Assert.isTrue(tempPost.getUserId() == getProfileId(),"无权限编辑此文章！");
        }
        postService.saveOrUpdate(post);
        return Result.succ("发布成功",null,"/post/" + post.getId());
    }
}
