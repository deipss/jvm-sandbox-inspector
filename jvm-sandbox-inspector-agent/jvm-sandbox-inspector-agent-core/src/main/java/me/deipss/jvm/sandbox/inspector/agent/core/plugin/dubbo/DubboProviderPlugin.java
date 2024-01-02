package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

@Slf4j
public class DubboProviderPlugin extends BasePlugin {
    public DubboProviderPlugin( InvocationSendService invocationSendService) {
        super(Constant.DUBBO_PROVIDER,true,invocationSendService);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.dubbo.rpc.filter.ContextFilter").includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new DubboProviderEventListener(entrance, protocol,invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
