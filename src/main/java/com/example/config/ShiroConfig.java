package com.example.config;

import com.example.shiro.AccountRealm;
import com.example.shiro.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro权限配置
 * @author cenkang
 * @Date 2019/12/29 - 0:19
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.shrio.host}")
    private String host; // redis的主机地址

    @Value("${spring.redis.shrio.database}")
    private int database;  // redis的数据库

    @Bean("securityManager")
    public SecurityManager securityManager(AccountRealm accountRealm, SessionManager sessionManager, CacheManager cacheManager){
        // 定义shiro安全管理器，并配置需要实现的功能
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置自定义session管理，使用redis
        securityManager.setSessionManager(sessionManager);
        // 实现cacheManager缓存功能
        securityManager.setCacheManager(cacheManager);
        // 实现自定义的realm功能
        securityManager.setRealm(accountRealm);

        log.info("------------->securityManager注入完成");
        return securityManager;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * Web应用中,Shiro可控制的Web请求必须经过Shiro主过滤器的拦截
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {

        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager,Shiro的核心安全接口
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url
        filterFactoryBean.setLoginUrl("/login");
        // 登录成功的url
        filterFactoryBean.setSuccessUrl("/index");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");

        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("auth", authFilter());
        // 统计登录人数
        filterFactoryBean.setFilters(filterMap);

        // 配置访问权限 必须是LinkedHashMap，因为它必须保证有序
        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 --> : 这是一个坑，一不小心代码就不好使了
        Map<String, String> hashMap = new LinkedHashMap<>();
        // anon:不需要验证，auth：需要验证，roles[admin]：需要admin权限
        hashMap.put("/login", "anon");
        hashMap.put("/post/edit", "auth");
        hashMap.put("/post/delete/", "auth");
        hashMap.put("/post/publish", "auth");
        hashMap.put("/comment/reply/", "auth");
        hashMap.put("/post/jieda-delete", "auth");
        hashMap.put("/collection/find/", "auth");
        hashMap.put("/user/jieda-zan/", "auth");
        hashMap.put("/admin/**", "auth, roles[admin]");
        hashMap.put("/user/**", "auth");
        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }


    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return 返回redis管理对象
     */
    @Bean
    RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // redis中针对不同用户缓存(此处的id需要对应user实体中的id字段,用于唯一标识)
        redisCacheManager.setPrincipalIdFieldName("id");
        // 用户权限信息缓存时间s
        redisCacheManager.setExpire(200000);
        return redisCacheManager;
    }

    @Bean
    RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setDatabase(database);
        return redisManager;
    }
}
