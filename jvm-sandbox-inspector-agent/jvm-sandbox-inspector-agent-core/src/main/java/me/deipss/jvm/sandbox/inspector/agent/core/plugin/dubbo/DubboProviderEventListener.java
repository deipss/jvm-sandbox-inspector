package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

@Slf4j
public class DubboProviderEventListener extends BaseEventListener {
    public DubboProviderEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        try {
            String span = MethodUtils.invokeMethod(event.argumentArray[1], "getAttachment", Span.SPAN).toString();
            return JSON.parseObject(span,Span.class);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error", e);
        }
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            invocation.setRequest((Object[]) MethodUtils.invokeMethod(event.argumentArray[1], "getArguments"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error", e);
        }
    }
}
