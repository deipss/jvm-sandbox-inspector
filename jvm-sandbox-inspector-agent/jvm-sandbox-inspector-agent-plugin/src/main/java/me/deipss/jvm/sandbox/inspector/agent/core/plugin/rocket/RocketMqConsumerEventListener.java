package me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class RocketMqConsumerEventListener extends BaseEventListener {
    public RocketMqConsumerEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol ,invocationSendService);
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        Object mqArg0 = event.argumentArray[0];
        try {
            Object msgExt = MethodUtils.invokeMethod(mqArg0, "get", 0);
            Object span = MethodUtils.invokeMethod(msgExt, "getUserProperty", Span.SPAN);
            return JSON.parseObject(span.toString(), Span.class);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("rocket mq extract span error", e);
            return null;
        }

    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        Object mqArg0 = event.argumentArray[0];
        try {
            Object msgExt = MethodUtils.invokeMethod(mqArg0, "get", 0);
            String topic = String.valueOf(MethodUtils.invokeMethod(msgExt, "getTopic"));
            String body = new String((byte[]) MethodUtils.invokeMethod(msgExt, "getBody"));
            invocation.setTopic(topic);
            invocation.setMqBody(body);
            if (msgExt.getClass().getCanonicalName().endsWith("MessageExt")) {
                String msgId = String.valueOf(MethodUtils.invokeMethod(msgExt, "getMsgId"));
                String tags = String.valueOf(MethodUtils.invokeMethod(msgExt, "getTags"));
                invocation.setMsgId(msgId);
                invocation.setTags(tags);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("rocket mq extract assembleRequest error", e);
        }
    }
}
