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
public class DubboConsumerPlugin extends BasePlugin   {


    @Override
    public String identify() {
        return Constant.DUBBO_CONSUMER;
    }

    @Override
    public boolean entrance() {
        return false;
    }

    @Override
    public int watch(ModuleEventWatcher watcher, InvocationSendService invocationSendService) {
        new EventWatchBuilder(watcher)
                // dubbo 2.0+
                .onClass("org.apache.dubbo.rpc.filter.ConsumerContextFilter").includeBootstrap()
                .onBehavior("invoke")
//                .onClass("org.apache.dubbo.rpc.filter.GenericImplFilter").includeBootstrap()
//                .onBehavior("invoke")
                // dubbo 3.0
                .onClass("org.apache.dubbo.rpc.cluster.filter.support.ConsumerContextFilter").includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new DubboConsumerEventListener(entrance(), identify(), invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
        return 1;
    }
}
