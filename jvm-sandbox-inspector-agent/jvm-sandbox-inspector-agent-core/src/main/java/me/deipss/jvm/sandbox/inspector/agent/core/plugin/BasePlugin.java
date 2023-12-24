package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public abstract class BasePlugin {

    public String protocol;

    public boolean entrance;

    public boolean enable(List<String> plugins){
//        return plugins.contains(protocol);
        return true;
    }

    public abstract void watch(ModuleEventWatcher watcher);
}
