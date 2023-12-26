package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

@Slf4j
public class DubboProviderPlugin extends BasePlugin {
    public DubboProviderPlugin() {
        super(Constant.DUBBO_PROVIDER,false);
        log.info("load plugin={},entrance={}", Constant.DUBBO_PROVIDER,false);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.dubbo.rpc.filter.ContextFilter").includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new DubboProviderEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
