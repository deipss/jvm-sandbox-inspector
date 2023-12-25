package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.alibaba.jvm.sandbox.api.event.ThrowsEvent;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import me.deipss.jvm.sandbox.inspector.agent.core.util.InvocationSendUtil;

@AllArgsConstructor
@Slf4j
public abstract class BaseEventListener implements EventListener {

    protected boolean entrance;

    protected String protocol;

    @Override
    public void onEvent(Event event) throws Throwable {
        try {
            switch (event.type) {
                case BEFORE:
                    String globalTraceId = extractSpan(((BeforeEvent) event));
                    if(entrance){
                        if(Strings.isNullOrEmpty(globalTraceId)) {
                            Tracer.start(((BeforeEvent) event).invokeId, protocol);
                        }else {
                            Tracer.start(globalTraceId,protocol,((BeforeEvent) event).invokeId);

                        }
                    }
                    doBefore((BeforeEvent) event);
                    transportSpan((BeforeEvent) event);
                    break;
                case RETURN:
                    doReturn((ReturnEvent) event);
                    if (entrance) {
                        Tracer.end(protocol, ((ReturnEvent) event).invokeId);
                    }
                    sendInvocationReturn((ReturnEvent) event);
                    break;
                case THROWS:
                    doThrow((ThrowsEvent) event);
                    if (entrance) {
                        Tracer.end(protocol, ((ThrowsEvent) event).invokeId);
                    }
                    sendInvocationThrow((ThrowsEvent) event);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("error ", e);
        }
    }

    public void doBefore(BeforeEvent event) {
        Invocation invocation = initInvocation(event);
        InvocationCache.put(event.invokeId, invocation);
    }

    public void doReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.get(event.invokeId);
        invocation.setResponse(event.object);
    }

    public void doThrow(ThrowsEvent event) {
        Invocation invocation = InvocationCache.get(event.invokeId);
        invocation.setThrowable(event.throwable);
        invocation.setThrowableMsg(event.throwable.getMessage());
        invocation.setThrowableClass(event.throwable.getClass().getCanonicalName());
    }

    public Invocation initInvocation(BeforeEvent event) {
        Invocation invocation = new Invocation();
        invocation.setInnerEntrace(entrance);
        invocation.setProtocol(protocol);
        invocation.setIndex(event.invokeId);
        invocation.setUk(Tracer.initTraceId(event.invokeId));
        invocation.setPreUk(Tracer.getPreUk());
        invocation.setIp(Tracer.getLocalIp());
        invocation.setTraceId(Tracer.getTraceId());
        return invocation;
    }

    public void sendInvocationReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        InvocationSendUtil.send(invocation);
    }

    public void sendInvocationThrow(ThrowsEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        InvocationSendUtil.send(invocation);
    }


    public abstract void transportSpan(BeforeEvent event);
    public abstract String extractSpan(BeforeEvent event);
}

