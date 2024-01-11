package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.api.spi.InspectorPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;
import org.kohsuke.MetaInfServices;

@Slf4j
@MetaInfServices(InspectorPlugin.class)
public class DubboProviderPlugin extends BasePlugin   {


    public String identify() {
        return Constant.DUBBO_PROVIDER;
    }

    @Override
    public boolean entrance() {
        return true;
    }

    @Override
    public int watch(ModuleEventWatcher watcher, InvocationSendService invocationSendService) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.dubbo.rpc.filter.ContextFilter").includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new DubboProviderEventListener(entrance(), identify(), invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
        return 1;
    }
}
