package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

public class RocketMqSendPlugin extends BasePlugin {
    public RocketMqSendPlugin() {
        super(Constant.DUBBO_CONSUMER,false);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl").includeBootstrap()
                .onBehavior("sendDefaultImpl")
                .onWatch(new RocketMqSendEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
