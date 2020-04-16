package com.example.templates;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.template.DirectiveHandler;
import com.example.common.template.TemplateDirective;
import com.example.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义freemark标签
 * 继承templateDirective类，重写execute方法
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

    /**
     *
     * @param handler
     * @throws Exception
     */
    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        // 获取导航栏id
        Long categoryId = handler.getLong("categoryId",0);
        // 获取当页数
        int pn = handler.getInteger("pn",1);
        // 获取每页数量
        int size = handler.getInteger("size",5);
        // 排序字段
        String order = handler.getString("order","created");

        Page page = new Page(pn,size);
        // 获取置顶帖子
        IPage results = postService.paging(page,null,categoryId,1,null,order);
        handler.put(RESULTS, results).render();
    }
}
