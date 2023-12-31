package me.deipss.jvm.sandbox.inspector.debug.provider.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDubbo(scanBasePackages={"me.deipss.jvm.sandbox.inspector.debug.provider.service"})
public class DubboConfig {
}
