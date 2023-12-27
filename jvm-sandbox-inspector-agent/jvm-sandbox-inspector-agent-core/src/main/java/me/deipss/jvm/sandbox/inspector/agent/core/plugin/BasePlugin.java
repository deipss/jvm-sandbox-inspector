package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;

import java.util.List;

@Slf4j
public abstract class BasePlugin {

    public String protocol;

    public boolean entrance;

    public boolean enable(List<String> plugins){
//        return plugins.contains(protocol);
        return true;
    }

    public BasePlugin(String protocol, boolean entrance) {
        this.protocol = protocol;
        this.entrance = entrance;
        log.info("load plugin={},entrance={}", protocol,false);

    }

    public abstract void watch(ModuleEventWatcher watcher);
}
