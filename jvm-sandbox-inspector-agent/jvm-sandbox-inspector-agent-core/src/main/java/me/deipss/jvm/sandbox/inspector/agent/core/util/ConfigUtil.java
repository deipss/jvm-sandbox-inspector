package me.deipss.jvm.sandbox.inspector.agent.core.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.GlobalConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class ConfigUtil {

    static {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("global_config.json");
        try {
            config = JSON.parseObject(resourceAsStream, GlobalConfig.class);
        } catch (IOException e) {
            log.error("global_config init error", e);
        }

    }

    private static GlobalConfig config;

    public static String getHeartUrl() {
        return config.getHeartUrl();
    }

    public static String getDataSendUrl() {
        return config.getDataSendUrl();
    }

    public static List<String> getEnablePlugins() {
        return config.getEnablePlugins();
    }
}
