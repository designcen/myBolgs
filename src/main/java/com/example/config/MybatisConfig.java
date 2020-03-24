package com.example.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis-plus配置
 * @author cenkang
 * @Date 2019/12/26 - 23:27
 */

@EnableTransactionManagement // 开启事务支持。访问数据库的Service方法上添加注解 @Transactional 便可实现事务
@MapperScan("com.example.mapper")  // 指定映射扫描的包
@Configuration
public class MybatisConfig {}
