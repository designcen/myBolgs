package com.example.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author cenkang
 * @date 2019/12/26 - 23:27
 */

@EnableTransactionManagement
@MapperScan("com.example.mapper")  // 指定映射扫描的包
@Configuration
public class MybatisConfig {
}
