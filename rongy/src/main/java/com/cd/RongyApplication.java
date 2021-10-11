package com.cd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mayn
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.cd.dao")  // 扫描dao接口
public class RongyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RongyApplication.class, args);
    }

}
