package com.example.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author cenkang
 * @Date 2019/12/26 - 22:18
 */
public class BaseController {

    @Autowired
    HttpServletRequest req;


    public Page getPage(){
        //当前页数
        int pn = ServletRequestUtils.getIntParameter(req,"pn",1);
        // 每页几条数据
        int size = ServletRequestUtils.getIntParameter(req,"size",10);
        Page page = new Page(pn,size);
        return page;
    }
}
