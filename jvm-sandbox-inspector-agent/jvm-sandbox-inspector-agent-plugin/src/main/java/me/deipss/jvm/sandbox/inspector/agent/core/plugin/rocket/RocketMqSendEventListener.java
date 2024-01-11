package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class RocketMqSendEventListener extends BaseEventListener {
    public RocketMqSendEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol,invocationSendService);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        for (Object o : event.argumentArray) {
            if (o.getClass().getSimpleName().equals("Message")) {
                Invocation invocation = InvocationCache.get(event.invokeId);
                Span span = new Span(Tracer.getTraceId(), invocation.getUk());
                try {
                    MethodUtils.invokeMethod(o, "putUserProperty", Span.SPAN, JSON.toJSONString(span));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("RocketMqSendEventListener transportSpan error",e );
                }
                return;
            }
        }
    }


    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        for (Object o : event.argumentArray) {
            if (o.getClass().getSimpleName().equals("Message")) {
                try {
                    String topic = String.valueOf(org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(o, "getTopic"));
                    String body = new String((byte[]) org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(o, "getBody"));
                    invocation.setTopic(topic);
                    invocation.setMqBody(body);
                    if (o.getClass().getCanonicalName().endsWith("MessageExt")) {
                        String tags = String.valueOf(org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(o, "getTags"));
                        String msgId = String.valueOf(org.apache.commons.lang3.reflect.MethodUtils.invokeMethod(o, "getMsgId"));
                        invocation.setMsgId(msgId);
                        invocation.setTags(tags);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("rocket mq extract assembleRequest error", e);
                }
                return;
            }
        }
    }
}