package me.deipss.jvm.sandbox.inspector.agent.core.plugins.rocket;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.api.spi.InspectorPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.BasePlugin;
import org.kohsuke.MetaInfServices;

@MetaInfServices(InspectorPlugin.class)
public class RocketMqSendPlugin extends BasePlugin {


    @Override
    public String identify() {
        return Constant.ROCKET_MQ_SEND;
    }

    @Override
    public boolean entrance() {
        return false;
    }

    @Override
    public int watch(ModuleEventWatcher watcher, InvocationSendService invocationSendService) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl").includeBootstrap()
                .onBehavior("sendDefaultImpl")
                .onWatch(new RocketMqSendEventListener(entrance(), identify(), invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
        return 0;
    }
}
