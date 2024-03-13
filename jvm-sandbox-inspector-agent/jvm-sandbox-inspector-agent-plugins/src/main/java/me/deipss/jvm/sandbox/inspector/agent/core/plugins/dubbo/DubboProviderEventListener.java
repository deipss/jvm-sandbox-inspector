package me.deipss.jvm.sandbox.inspector.agent.core.plugins.dubbo;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.BaseEventListener;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.dubbo.rpc.RpcContext;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class DubboProviderEventListener extends BaseEventListener {
    public DubboProviderEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol, invocationSendService);
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        try {
            Object spanObj = RpcContext.getContext().getAttachment(Span.SPAN);
            if (Objects.isNull(spanObj)) {
                log.error("DubboProviderEventListener extractSpan span is null");
                return null;
            }
            String span = spanObj.toString();
            return JSON.parseObject(span, Span.class);
        } catch (Exception e) {
            log.error("DubboProviderEventListener extractSpan error", e);
            return null;
        }
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // invoke(Invoker<?> invoker, Invocation invocation)
        try {
            Object args = event.argumentArray[1];
            Object invoker = event.argumentArray[0];
            invocation.setRequest((Object[]) MethodUtils.invokeMethod(args, "getArguments"));
            Map<String, String> attachments = RpcContext.getContext().getAttachments();
            invocation.setRpcContext(new HashMap<>(attachments.size()));
            invocation.getRpcContext().putAll(attachments);
            invocation.setMethodName(MethodUtils.invokeMethod(args, "getMethodName").toString());
            String interfaceName = ((Class<?>) MethodUtils.invokeMethod(invoker, "getInterface")).getCanonicalName();
            invocation.setClassName(interfaceName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo consumer assembleRequest error", e);
        }
    }


    @Override
    public void assembleResponse(ReturnEvent event, Invocation invocation) {
        try {
            invocation.setResponse(MethodUtils.invokeMethod(event.object, "getValue"));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("dubbo provider assembleResponse error", e);
        }
    }
}
