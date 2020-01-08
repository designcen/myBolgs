package com.example.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.*;
import com.example.shiro.AccountProfile;
import com.example.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cenkang
 * @Date 2020/1/7 15:55
 */
@Slf4j
@Controller
public class CenterController extends BaseController {

    @GetMapping("")
    public String index(){
        Page page = getPage();
        log.info("====================>进入个人中心");
        QueryWrapper<Post> wrapper = new QueryWrapper<Post>()
                .eq("user_id",getProfileId())
                .orderByDesc("created");
        IPage<Post> pageData = postService.page(page,wrapper);
        req.setAttribute("pageData",pageData);
        return "center/index";
    }

    @GetMapping("/collection")
    public String collection(){
        Page page = getPage();
        QueryWrapper queryWrapper = new QueryWrapper<>()
                .eq("u.user_id",getProfileId())
                .orderByDesc("created");
        IPage<Post> pageData = userCollectionService.paging(page,queryWrapper);
        req.setAttribute("pageData",pageData);
        return "center/collection";
    }
    @GetMapping("/setting")
    public String setting(){
        User user = userService.getById(getProfileId());
        user.setPassword(null);
        req.setAttribute("user",user);
        return "center/setting";
    }

    @PostMapping("setting")
    @ResponseBody
    public Result postSetting(User user){
        if (StringUtils.isEmpty(user.getUsername())) {
            return Result.fail("用户名不能为空");
        }
        User tempUser = userService.getById(getProfileId());
        tempUser.setUsername(user.getUsername());
        tempUser.setGender(user.getGender());
        tempUser.setSign(user.getSign());
        boolean isSucc = userService.updateById(tempUser);
        if (isSucc) {
            // 更新shiro信息
            AccountProfile profile = getProfile();
            profile.setUsername(user.getUsername());
            profile.setGender(user.getGender());
        }
        return isSucc ? Result.succ("更新成功",null,"/center/setting") : Result.fail("更新失败");
    }

    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam(value = "file")MultipartFile file,@RequestParam(name = "type",defaultValue = "avatar") String type){
        if (file.isEmpty()) {
            return Result.fail("上传失败");
        }
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        String filePath = constant.getUploadDir();
        if ("avatar".equalsIgnoreCase(type)) {
            fileName = "/avatar/avatar_" + getProfile() + suffixName;
        }else if ("post".equalsIgnoreCase(type)) {
            fileName = "/post/post_" + DateUtil.format(new Date(),
                    DatePattern.PURE_DATETIME_FORMAT) + suffixName;
        }
        File dest = new File(filePath + fileName);
        // 检测是否存在目标
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            // 文件复制就这一行代码重要
            file.transferTo(dest);
            log.info("上传成功后的文件路径为：" + filePath + fileName);
            String path = filePath + fileName;
            String url = constant.getUploadUrl() + fileName;
            log.info("url--------------> {}",url);
            User current = userService.getById(getProfileId());
            current.setAvatar(url);
            userService.updateById(current);
            // 更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setAvatar(url);
            return Result.succ(url);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return Result.succ(null);
    }

    @PostMapping("repass")
    @ResponseBody
    public Result repass(String oldPass,String newPass,String reNewPass){
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
        return Result.succ("更新成功",null,"/center/setting");
    }

    @GetMapping("/message")
    public String message(){
        Page<UserMessage> page = getPage();
        QueryWrapper wrapper = new QueryWrapper<UserMessage>()
                .eq("to_user_id",getProfile())
                .orderByDesc("created");
        IPage<MessageVo> pageData = userMessageService.paging(page,wrapper);
        req.setAttribute("pageData",pageData);
        return "center/message";
    }

    @PostMapping("/message/remove")
    @ResponseBody
    public Result removeMsg(Long id,boolean all){
        QueryWrapper<UserMessage> wrapper = new QueryWrapper<UserMessage>()
                .eq("to_user_id",getProfile())
                .eq(!all,"id",id);

        // 只能删除自己的消息
        boolean res = userMessageService.remove(wrapper);
        return res ?
                Result.succ("操作成功",null,"/center/message") : Result.fail("删除失败");
    }

    @GetMapping("/post/edit")
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

    @PostMapping("/post/submit")
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
            Assert.isTrue(tempPost.getUserId() == getProfileId(),"无权限编辑此文章！");
        }
        postService.saveOrUpdate(post);
        return Result.succ("发布成功",null,"/post/" + post.getId());
    }

    @PostMapping("/post/delete")
    @Transactional
    @ResponseBody
    public Result delete(Long id){
        Post post = postService.getById(id);
        Assert.notNull(post,"该帖子已被删除");
        Assert.isTrue(post.getUserId() == getProfileId(),"无权限删除此文章！");
        postService.removeById(id);
        // 删除相关消息，收藏等
        userMessageService.removeByMap(MapUtil.of("post_id", id));
        userCollectionService.removeByMap(MapUtil.of("post_id",id));
        return Result.succ("删除成功",null,"/center");
    }

    @PostMapping("/post/reply")
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

    @PostMapping("/post/jieda-delete/")
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
}
