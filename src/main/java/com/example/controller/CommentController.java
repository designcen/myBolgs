package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.common.lang.Result;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.UserMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 评论操作
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {
    /**
     * 删除自己的某一条评论
     * @param id 评论id
     * @return
     */
    @PostMapping("/jieda-delete/")
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

        // redis缓存中评论数量减一
        postService.incrZsetValueAndUnionForLastWeekRank(comment.getPostId(), false);

        return Result.succ(null);
    }


    /**
     * 评论帖子
     *
     * @param pid      帖子id
     * @param parentId 被回复评论的id
     * @param content  评论内容
     * @return
     */
    @PostMapping("/reply/")
    @Transactional
    @ResponseBody
    public Result reply(Long pid, Long parentId, String content) {
        Assert.notNull(pid, "找不到对应文章！");
        Assert.hasLength(content, "评论内容不能为空！");
        Post post = postService.getById(pid);
        Assert.isTrue(post != null, "该文章已被删除");
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setPostId(pid);
        comment.setUserId(getProfileId());
        comment.setVoteUp(0);
        comment.setVoteDown(0);
        comment.setLevel(0);
        comment.setCreated(new Date());
        comment.setModified(new Date());
        // 保存评论并更新缓存
        commentService.saveAndUpdate(getPage(),comment,"id");
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
            wsService.sendMessCountToUser(message.getToUserId(), null);
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
                wsService.sendMessCountToUser(message2.getToUserId(), null);
            }
        }
        return Result.succ("评论成功", null, "/post/" + pid);
    }
}
