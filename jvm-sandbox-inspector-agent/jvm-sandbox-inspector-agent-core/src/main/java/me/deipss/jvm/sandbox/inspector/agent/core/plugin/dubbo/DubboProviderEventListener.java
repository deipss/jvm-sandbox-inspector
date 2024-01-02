package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.rpc.RpcContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
            Object o = event.argumentArray[1];
            if (o.getClass().getCanonicalName().endsWith("Invocation")) {
                Object spanObj = MethodUtils.invokeMethod(o, "getAttachment", Span.SPAN);
                if (Objects.isNull(spanObj)) {
                    log.error("DubboProviderEventListener extractSpan span is null");
                    return null;
                }
                String span = spanObj.toString();
                return JSON.parseObject(span, Span.class);
            } else {
                log.error("DubboProviderEventListener extractSpan error, argumentArray1 not Invocation but ={} ", o.getClass().getCanonicalName());
            }
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
            ClassLoader sandboxClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(event.javaClassLoader);
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            invocation.setRpcContext(new HashMap<>(attachments.size()));
            invocation.getRpcContext().putAll(attachments);
            Thread.currentThread().setContextClassLoader(sandboxClassLoader);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error", e);
        }
    }
}
