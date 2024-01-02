package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

public class RocketMqSendPlugin extends BasePlugin {
    public RocketMqSendPlugin(InvocationSendService invocationSendService) {
        super(Constant.ROCKET_MQ_SEND,false,invocationSendService);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl").includeBootstrap()
                .onBehavior("sendDefaultImpl")
                .onWatch(new RocketMqSendEventListener(entrance, protocol,invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
