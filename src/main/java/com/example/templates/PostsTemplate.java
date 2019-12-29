package com.example.templates;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.template.DirectiveHandler;
import com.example.common.template.TemplateDirective;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author cenkang
 * @date 2019/12/29 - 11:25
 */
@Component
public class PostsTemplate extends TemplateDirective {

    @Autowired
    PostService postService;
    @Override
    public String getName() {
        return "posts";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        Long categoryId = handler.getLong("categoryId",0);
        int pn = handler.getInteger("pn",1);
        int size = handler.getInteger("size",10);
        String order = handler.getString("order","create");

        Page page = new Page(pn,size);
        IPage results = postService.paging(page,categoryId,order);
    }
}
