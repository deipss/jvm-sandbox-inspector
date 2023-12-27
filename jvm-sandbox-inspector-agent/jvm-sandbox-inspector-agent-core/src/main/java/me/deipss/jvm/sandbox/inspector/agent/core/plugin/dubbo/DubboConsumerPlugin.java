package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

@Slf4j
public class DubboConsumerPlugin extends BasePlugin {
    public DubboConsumerPlugin() {
        super(Constant.DUBBO_CONSUMER,false);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                // dubbo 2.0+
                .onClass("org.apache.dubbo.rpc.filter.ConsumerContextFilter").includeBootstrap()
                .onBehavior("invoke")
//                .onClass("org.apache.dubbo.rpc.filter.GenericImplFilter").includeBootstrap()
//                .onBehavior("invoke")
                // dubbo 3.0
                .onClass("org.apache.dubbo.rpc.cluster.filter.support.ConsumerContextFilter").includeBootstrap()
                .onBehavior("invoke")
                .onWatch(new DubboConsumerEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
