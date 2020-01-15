package com.example.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vo.PostVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  导航栏分类
 * </p>
 *
 * @author cenkang
 * @since 2019-12-26
 */
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    /**
     *  导航分类
     * @param id 分类id
     * @return
     */
    @GetMapping("/{id:\\d*}")
    public String category(@PathVariable Long id){
        Page page = getPage();
        IPage<PostVo> pageData = postService.paging(page,null,id,null,null,"created");
        req.setAttribute("pageData",pageData);
        // currentCategoryId是为了回显当前选择的栏目
        req.setAttribute("currentCategoryId",id);
        return "post/category";
    }
}
