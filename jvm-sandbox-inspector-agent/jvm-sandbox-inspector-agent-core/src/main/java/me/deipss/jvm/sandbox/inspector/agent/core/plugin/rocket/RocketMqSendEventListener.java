package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import org.apache.dubbo.common.utils.MethodUtils;

@Slf4j
public class RocketMqSendEventListener extends BaseEventListener {
    public RocketMqSendEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        for (Object o : event.argumentArray) {
            if(o.getClass().getSimpleName().equals("Message")){
                Invocation invocation = InvocationCache.get(event.invokeId);
                Span span = new Span(Tracer.getTraceId(), invocation.getUk());
                MethodUtils.invokeMethod(o,"putUserProperty",Span.SPAN, JSON.toJSONString(span));
                return;
            }
        }
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        for (Object o : event.argumentArray) {
            if(o.getClass().getSimpleName().equals("Message")){
                invocation.setRequest(new Object[]{o});
                return;
            }
        }
    }
}
