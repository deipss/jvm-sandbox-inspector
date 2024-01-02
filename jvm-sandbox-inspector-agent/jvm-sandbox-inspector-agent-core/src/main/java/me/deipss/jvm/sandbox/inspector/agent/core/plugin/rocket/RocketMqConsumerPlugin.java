package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

public class RocketMqConsumerPlugin extends BasePlugin {
    public RocketMqConsumerPlugin(InvocationSendService invocationSendService) {
        super(Constant.ROCKET_MQ_CONSUMER,false,invocationSendService);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently").includeBootstrap()
                .includeSubClasses()
                .onBehavior("consumeMessage")
                .onClass("org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly").includeBootstrap()
                .includeSubClasses()
                .onBehavior("consumeMessage")
                .onWatch(new RocketMqConsumerEventListener(entrance, protocol,invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
