package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;

@Slf4j
public class DubboConsumerEventListener extends BaseEventListener {
    public DubboConsumerEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        log.info(event.javaClassName);
    }
}
