package com.xiaoxu.xBlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan  //使用servlet包扫描
@EnableTransactionManagement  //开启事务管理
@EnableCaching  //开启Spring Cache
public class xBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(xBlogApplication.class, args);
        log.info("基于Spring，SpringBoot，Mybatis-Plus设计");
        log.info("xBlog 项目启动成功！！！");
    }

}
