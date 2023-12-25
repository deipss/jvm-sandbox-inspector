package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

public class RocketMqConsumerPlugin extends BasePlugin {
    public RocketMqConsumerPlugin() {
        super(Constant.DUBBO_CONSUMER,false);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently").includeBootstrap()
                .onBehavior("consumeMessage")
                .onClass("org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly").includeBootstrap()
                .onBehavior("consumeMessage")
                .onWatch(new RocketMqConsumerEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
