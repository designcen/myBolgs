package com.example.config;

import com.example.common.lang.Constant;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，可以自定义一些Handler，Interceptor，ViewResolver，MessageConverter
 * @author cenkang
 * @date 2019/12/29 - 10:43
 */
@EnableScheduling // 定时任务开启
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    Constant constant;

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.image.height", "38");
        properties.put("kaptcha.image.width", "150");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.font.size", "32");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    /**
     * 因为上传的图片位置是存放在根目录下的，
     * 这涉及到一些静态资源的加载问题，所以需要在mvc配置中添加这个静态资源的位置
     * addResoureHandler：指的是对外暴露的访问路径,addResourceLocations：指的是内部文件放置的目录
     *
     * @param registry 静态资源处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加本地方问
        registry.addResourceHandler("/upload/avatar/**")
                .addResourceLocations("file:///" + constant.getUploadDir() + "/avatar/");
    }
}
