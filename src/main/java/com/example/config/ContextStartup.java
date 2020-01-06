package com.example.config;

import com.example.service.CategoryService;
import com.example.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

@Slf4j
@Order(1000)  // 指定加载的优先级
@Component
public class ContextStartup implements ApplicationRunner, ServletContextAware {

    private ServletContext servletContext;
    @Autowired
    CategoryService categoryser;
    @Autowired
    PostService postService;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化将路径放入servlet上下文中
        servletContext.setAttribute("base", servletContext.getContextPath());
        // 初始化首页周评论排行榜
        postService.initIndexWeekRank();
        log.info("ContextStartup============================>加载本周热议");
        // 首页导航分类是所有的地方都用到的，所以导航分类信息初始化到servletContext中，只启动初始化一次即可
        servletContext.setAttribute("categorys",categoryser.list(null));
        // currentCategoryId是为了回显当前选择的分类，默认为0（首页）
        servletContext.setAttribute("currentCategoryId",0);
        log.info("ContextStartup========================>加载categorys");

    }
}
