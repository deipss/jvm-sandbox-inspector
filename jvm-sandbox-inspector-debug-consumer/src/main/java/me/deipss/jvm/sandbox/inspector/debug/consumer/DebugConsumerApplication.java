package me.deipss.jvm.sandbox.inspector.debug.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class} ,
        scanBasePackages="me.deipss.jvm.sandbox.inspector.debug.consumer")
@EnableDubbo
@EnableConfigurationProperties
@Slf4j
public class DebugConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebugConsumerApplication.class, args);
        log.info("DebugConsumerApplication start success");
    }
}
