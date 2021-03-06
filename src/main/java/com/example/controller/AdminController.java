package com.example.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.lang.Result;
import com.example.entity.Post;
import com.example.service.SearchService;
import com.example.vo.PostVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cenkang
 * @Date 2020/2/19 15:16
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    SearchService searchService;

    /**
     * 初始化索引库
     * @return
     */
    @ResponseBody
    @PostMapping("/initEsData")
    public Result initEsData() {

        int total = 0;

        int size = 10000;
        Page page = new Page<>();

        for(int i = 1; i < 1000; i ++) {

            page.setCurrent(i);
            page.setSize(size);
            IPage<PostVo> paging = postService.paging(page, null, null, null, null, "created");

            int num = searchService.initEsIndex(paging.getRecords());

            total += num;
            if(num < size) {
                break;
            }
        }
        return Result.succ("ES索引库初始化成功！共" + total + "条记录", null);
    }

    /**
     * 设置文章是否置顶、是否加精、删除
     * @param id 文章id
     * @param rank
     * @param field 操作内容（置顶、加精、删除）
     * @return
     */
    @ResponseBody
    @Transactional
    @PostMapping("/set")
    public Result jieSet(Long id, Integer rank, String field) {

        Post post = postService.getById(id);
        Assert.isTrue(post != null, "该文章已被删除");

        if ("delete".equals(field)) {
            postService.removeById(id);
            // 删除缓存分页文章
            postService.deletePagingCache();
            return Result.succ("删除成功", null, "/index");

        } else if ("stick".equals(field)) {
            post.setLevel(rank);
        } else if ("status".equals(field)) {
            post.setRecommend(rank == 1);
        }
        postService.updateById(post);
        // 删除缓存分页文章
        postService.deletePagingCache();
        return Result.succ(null);
    }
}