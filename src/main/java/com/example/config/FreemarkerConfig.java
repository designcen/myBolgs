package com.example.config;

import com.example.templates.HotsTemplate;
import com.example.templates.PostsTemplate;
import com.jagregory.shiro.freemarker.ShiroTags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * freemarker模板引擎配置
 * @author cenkang
 * @Date 2019/12/29 - 13:31
 */
@Configuration
public class FreemarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration; // 全局共享变量，公共配置信息对象

    @Autowired
    private ApplicationContext applicationContext; // 是BeanFactory的子接口，也是Bean容器

    @PostConstruct
    public void setUp(){
        configuration.setSharedVariable("hots",applicationContext.getBean(HotsTemplate.class));
        configuration.setSharedVariable("posts", applicationContext.getBean(PostsTemplate.class));
        // 把shiro的标签注入到freemarker的标签配置中
        configuration.setSharedVariable("shiro",new ShiroTags());
    }
}
