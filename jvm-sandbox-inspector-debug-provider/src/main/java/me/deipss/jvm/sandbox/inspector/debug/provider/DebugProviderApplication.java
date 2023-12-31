package me.deipss.jvm.sandbox.inspector.debug.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DubboAutoConfiguration.class} ,scanBasePackages="me.deipss.jvm.sandbox.inspector.debug.provider")
@EnableConfigurationProperties
@Slf4j
public class DebugProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebugProviderApplication.class, args);
        log.info("DebugProviderApplication start success");
    }
}
