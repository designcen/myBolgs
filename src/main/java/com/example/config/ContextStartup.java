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

/**
 * 项目启动容器配置
 * 实现ServletContextAware,ApplicationRunner接口，他们的执行时机为容器启动完成的时候
 * 重写setServletContext，run方法
 */
@Slf4j
@Order(1000)  // 指定加载的优先级
@Component
public class ContextStartup implements ServletContextAware,ApplicationRunner {

    private ServletContext servletContext;
    @Autowired
    CategoryService categoryser;
    @Autowired
    PostService postService;

    /**
     * 注入servlet上下文
     * @param servletContext servlet上下文
     */
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /**
     * 指定项目启动后执行的方法
     * @param args 应用程序中需要访问传递给SpringApplication.run()的应用程序参数
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化将路径放入servlet上下文中
        servletContext.setAttribute("base", servletContext.getContextPath());
        // 初始化首页周评论排行榜
        postService.initIndexWeekRank();
        log.info("ContextStartup============================>加载本周热议完成");
        // 首页导航分类是所有的地方都用到的，所以导航分类信息初始化到servletContext中，只启动初始化一次即可
        servletContext.setAttribute("categorys",categoryser.list(null));
        // currentCategoryId是为了回显当前选择的分类，默认为0（首页）
        servletContext.setAttribute("currentCategoryId",0);
        log.info("ContextStartup========================>加载首页导航栏categorys完成");

    }
}
