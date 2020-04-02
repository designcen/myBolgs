package com.example.controller;


import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.*;
import com.example.shiro.AccountProfile;
import com.example.vo.CollectionVo;
import com.example.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 用户中心
 * @author cenkang
 * @Date 2020/1/7 15:55
 */
@Slf4j
@Controller
@RequestMapping("/center")
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
        IPage<CollectionVo> pageData = userCollectionService.paging(page,queryWrapper);
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

    @PostMapping("/setting")
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

    @PostMapping("/repass")
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

    @PostMapping("/remove")
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
}
