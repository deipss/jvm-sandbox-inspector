package me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
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


    public DubboConsumerEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol,invocationSendService);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        ClassLoader sandboxClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(event.javaClassLoader);
            log.info("DubboConsumerEventListener transportSpan, event classloader={},thread classloader={}",event.javaClassLoader.getClass().getCanonicalName(),sandboxClassLoader.getClass().getCanonicalName());
            Invocation invocation = InvocationCache.get(event.invokeId);
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            invocation.setRpcContext(new HashMap<>(attachments.size()));
            invocation.getRpcContext().putAll(attachments);
            Span span = new Span(Tracer.getTraceId(), invocation.getUk());
            RpcContext.getContext().setAttachment(Span.SPAN, JSON.toJSONString(span));
        } catch (Exception e) {
            log.error("DubboConsumerEventListener transportSpan error, eventId={}",event.invokeId,e );
        }finally {
            Thread.currentThread().setContextClassLoader(sandboxClassLoader);
        }
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            Object args = event.argumentArray[1];
            invocation.setRequest((Object[]) MethodUtils.invokeMethod(args, "getArguments"));
            invocation.setMethodName(MethodUtils.invokeMethod(args, "getMethodName").toString());
            invocation.setClassName(MethodUtils.invokeMethod(args, "getTargetServiceUniqueName").toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error",e);
        }
    }

    @Override
    public void assembleResponse(ReturnEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            invocation.setResponse(MethodUtils.invokeMethod(event.object, "getValue"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleResponse error",e);
        }
    }

}
