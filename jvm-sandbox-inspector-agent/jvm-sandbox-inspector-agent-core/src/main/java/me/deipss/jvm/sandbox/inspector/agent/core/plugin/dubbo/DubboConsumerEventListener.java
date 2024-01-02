package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.rpc.RpcContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DubboConsumerEventListener extends BaseEventListener {


    public DubboConsumerEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        try {
            ClassLoader sandboxClassLoader = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(event.javaClassLoader);
            Invocation invocation = InvocationCache.get(event.invokeId);
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            invocation.setRpcContext(new HashMap<>(attachments.size()));
            invocation.getRpcContext().putAll(attachments);
            Span span = new Span(Tracer.getTraceId(), invocation.getUk());
            RpcContext.getContext().setAttachment(Span.SPAN, JSON.toJSONString(span));
            Thread.currentThread().setContextClassLoader(sandboxClassLoader);
        } catch (Exception e) {
            log.error("DubboConsumerEventListener transportSpan error, eventId={}",event.invokeId,e );
        }
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            invocation.setRequest((Object[]) MethodUtils.invokeMethod(event.argumentArray[1], "getArguments"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error",e);
        }
    }

}
