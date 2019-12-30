package com.example.config;

import com.example.templates.PostsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author cenkang
 * @date 2019/12/29 - 13:31
 */
@Configuration
public class FreemarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void setUp(){
        configuration.setSharedVariable("posts", applicationContext.getBean(PostsTemplate.class));
    }
}
