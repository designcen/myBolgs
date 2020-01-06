package com.example.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import org.springframework.cache.annotation.EnableCaching;
/**
 * @author cenkang
 * @date 2019/12/29 - 10:43
 */
@EnableCaching      // 开启一下我们的缓存注解功能
@Configuration
public class WebMvcConfig {

    @Bean
    public DefaultKaptcha producer(){
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
}
