package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;

@Slf4j
public class RocketMqConsumerEventListener extends BaseEventListener {
    public RocketMqConsumerEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        log.info(event.javaClassName);
    }

    @Override
    public String extractSpan(BeforeEvent event) {
        return null;
    }
}
