package com.zhai.picshared;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@MapperScan("com.zhai.picshared.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})

public class PicSharedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PicSharedApplication.class, args);
    }

}
