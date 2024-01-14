package me.deipss.jvm.sandbox.inspector.agent.core.plugins;

import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.spi.InspectorPlugin;

import java.util.List;

@Slf4j
public abstract class BasePlugin implements InspectorPlugin {


    public boolean enable(List<String> plugins){
//        return plugins.contains(protocol);
        return true;
    }
}
