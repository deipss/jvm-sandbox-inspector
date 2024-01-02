package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;

import java.util.List;

@Slf4j
public abstract class BasePlugin {

    public String protocol;

    public boolean entrance;
    public InvocationSendService invocationSendService;

    public boolean enable(List<String> plugins){
//        return plugins.contains(protocol);
        return true;
    }

    public BasePlugin(String protocol, boolean entrance,InvocationSendService invocationSendService) {
        this.protocol = protocol;
        this.entrance = entrance;
        this.invocationSendService = invocationSendService;
        log.info("load plugin={},entrance={}", protocol,false);
    }

    public abstract void watch(ModuleEventWatcher watcher);
}
