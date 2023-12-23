package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import java.util.List;

public abstract class BasePlugin {

    public String pluginTag;


    public boolean enable(List<String> plugins){
        return plugins.contains(pluginTag);
    }
}
