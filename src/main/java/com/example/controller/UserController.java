package com.example.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Comment;
import com.example.entity.Post;
import com.example.entity.User;
import com.example.entity.UserCollection;
import com.example.shiro.AccountProfile;
import com.example.vo.CollectionVo;
import com.example.vo.CommentVo;
import com.example.vo.PostVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户基本信息
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    /**
     * 跳转用户主页，可能需要查看他人的主页，所以此处需要有用户对应的id
     *
     * @param id 用户id
     * @return
     */
    @RequestMapping("/{id:\\d*}")
    public String home(@PathVariable Long id) {
        User user = userService.getById(id);
        user.setPassword(null);
        // 30天文章的内容
        Date date30Before = DateUtil.offsetDay(new Date(), -30).toJdkDate();
        List<Post> posts = postService.list(new QueryWrapper<Post>()
                .eq("user_id", id)
                .ge("created", date30Before)
                .orderByDesc("created"));
        Date time24Before = DateUtil.offsetHour(new Date(), -24).toJdkDate();
        // 最近24小时的评论
        IPage<CommentVo> commentList = commentService.getLateComment(getPage(), new QueryWrapper<Comment>()
                .eq("c.user_id", id)
                .ge("c.created", time24Before)
                .orderByDesc("c.created"));
        req.setAttribute("user", user);
        req.setAttribute("posts", posts);
        req.setAttribute("pageData", commentList);
        return "user/home";
    }

    /**
     * 跳转到用户中心并展示当前用户所有的发帖（文章）
     *
     * @return
     */
    @GetMapping("index")
    public String index() {
        // 获取当前shiro中的用户
        AccountProfile suser = getProfile();
        // 分页获取当前用户的所有文章
        IPage<PostVo> postAll = postService.paging(getPage(), suser.getId(), null, suser.getVipLevel(), null, "created");
        // 获取收藏数量
        int collectionCount = userCollectionService.count(new QueryWrapper<UserCollection>().eq("user_id", getProfileId()));
        // 获取发帖（文章）数量
        int postCount = postService.count(new QueryWrapper<Post>().eq("user_id", getProfileId()));

        req.setAttribute("pageData", postAll);
        req.setAttribute("collectionCount", collectionCount);
        req.setAttribute("postCount", postCount);
        return "user/index";
    }

    /**
     * 查询我收藏的帖子（文章）
     *
     * @param
     * @return
     */
    @GetMapping("/find")
    public String collectionFind() {
        // 获取分页
        Page page = getPage();
        // 获取当前用户收藏的文章
        IPage<CollectionVo> collectionList = userCollectionService
                .paging(page, new QueryWrapper<UserCollection>().eq("u.user_id", getProfileId()));
        // 获取收藏数量
        int collectionCount = userCollectionService
                .count(new QueryWrapper<UserCollection>().eq("user_id", getProfileId()));
        // 获取发帖（文章）数量
        int postCount = postService
                .count(new QueryWrapper<Post>().eq("user_id", getProfileId()));
        req.setAttribute("pageData", collectionList);
        req.setAttribute("collectionCount", collectionCount);
        req.setAttribute("postCount", postCount);
        return "user/find";
    }

    /**
     * 跳转到用户设置
     *
     * @return
     */
    @GetMapping("/set")
    public String setting() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);
        req.setAttribute("user", user);
        return "user/set";
    }

    @PostMapping("/repass")
    @ResponseBody
    public Result repass(String oldPass, String newPass, String reNewPass) {
        if (!newPass.equals(reNewPass)) {
            return Result.fail("两次密码不一致");
        }
        User user = userService.getById(getProfileId());
        String oldPassMd5 = SecureUtil.md5(oldPass);
        if (!oldPassMd5.equals(user.getPassword())) {
            return Result.fail("原密码不正确");
        }
        user.setPassword(SecureUtil.md5(newPass));
        userService.updateById(user);
        return Result.succ("更新成功", null, "/user/set");
    }

    /**
     * 跳转到用户设置
     *
     * @return
     */
    @GetMapping("/active")
    public String active() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);
        req.setAttribute("user", user);
        return "user/active";
    }

    /**
     * 跳转到我的消息
     *
     * @return
     */
    @GetMapping("/message")
    public String message() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);
        req.setAttribute("user", user);
        return "user/message";
    }


    @PostMapping("/jieda-zan/")
    @ResponseBody
    public Result praise(boolean ok, Long id) {
        Comment comment = commentService.getById(id);
        Assert.notNull(comment,"该评论不存在");
        // 如果ok为true说明添加是点赞
        if (ok) {
            Integer voteUp = comment.getVoteUp() + 1;
            comment.setVoteUp(voteUp);
            commentService.updateById(comment);
        } else {
            Integer voteUp = comment.getVoteUp() - 1;
            comment.setVoteUp(voteUp);
            commentService.updateById(comment);
        }
        return Result.succ("操作成功", null);
    }

    @ResponseBody
    @PostMapping("/modified")
    public Result modified(User user) {

        if(StringUtils.isEmpty(user.getUsername())){
            return Result.fail("用户名不能为空");
        }

        User tempUser = userService.getById(getProfileId());
//        tempUser.setEmail(user.getEmail());
        tempUser.setUsername(user.getUsername());
        tempUser.setGender(user.getGender());
        tempUser.setSign(user.getSign());

        boolean isSucc = userService.updateById(tempUser);
        if(isSucc) {
            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setUsername(user.getUsername());
            profile.setGender(user.getGender());
        }

        return isSucc ? Result.succ("更新成功", null, "/user/set"): Result.fail("更新失败");
    }
}
