package me.deipss.jvm.sandbox.inspector.agent.core.plugins.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.BaseEventListener;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.rpc.RpcContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DubboConsumerEventListener extends BaseEventListener {


    public DubboConsumerEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol,invocationSendService);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
        try {
            log.info("DubboConsumerEventListener transportSpan, event classloader={},thread classloader={}",event.javaClassLoader.getClass().getCanonicalName(),Thread.currentThread().getClass().getCanonicalName());
            Invocation invocation = InvocationCache.get(event.invokeId);
            Span span = new Span(Tracer.getTraceId(), invocation.getUk());
            RpcContext.getContext().setAttachment(Span.SPAN, JSON.toJSONString(span));
            Map<String, String> attachmentMap = RpcContext.getContext().getAttachments();
            if(Objects.nonNull(attachmentMap)) {
                invocation.setRpcContext(new HashMap<>(attachmentMap.size()));
                invocation.getRpcContext().putAll(attachmentMap);
            }
        } catch (Exception e) {
            log.error("DubboConsumerEventListener transportSpan error, eventId={}",event.invokeId,e );
        }

    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            Object args = event.argumentArray[1];
            Object invoker = event.argumentArray[0];
            invocation.setRequest((Object[]) MethodUtils.invokeMethod(args, "getArguments"));
            invocation.setMethodName(MethodUtils.invokeMethod(args, "getMethodName").toString());
            String  interfaceName = ((Class)MethodUtils.invokeMethod(invoker, "getInterface")).getCanonicalName();
            invocation.setClassName(interfaceName);
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
