package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Constant;
import com.example.service.*;
import com.example.shiro.AccountProfile;
import com.example.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * @author cenkang
 * @Date 2019/12/26 - 22:18
 */
public class BaseController {

    @Autowired
    WsService wsService;

    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    PostService postService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    UserMessageService userMessageService;

    @Autowired
    UserCollectionService userCollectionService;

    @Autowired
    Constant constant;

    @Autowired
    ObjectMapper objectMapper;

    public Page getPage() {
        //当前页数
        int pn = ServletRequestUtils.getIntParameter(req, "pn", 1);
        // 每页几条数据
        int size = ServletRequestUtils.getIntParameter(req, "size", 10);
        Page page = new Page(pn, size);
        return page;
    }

    public long getProfileId() {
        return getProfile().getId();
    }

    public AccountProfile getProfile() {
        // 没有直接向下转型，原因是devtools类加载器与IDE的不同
        AccountProfile accountProfile = new AccountProfile();
        Object o = SecurityUtils.getSubject().getPrincipal();
        if (o != null) {
            try {
                PropertyUtils.copyProperties(accountProfile, o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return accountProfile;
    }
}
