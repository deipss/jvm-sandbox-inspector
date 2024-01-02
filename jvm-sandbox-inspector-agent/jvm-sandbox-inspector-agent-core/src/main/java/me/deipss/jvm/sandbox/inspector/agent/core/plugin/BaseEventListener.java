package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.alibaba.jvm.sandbox.api.event.ThrowsEvent;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;
import me.deipss.jvm.sandbox.inspector.agent.core.util.InvocationSendUtil;

import java.util.Date;
import java.util.Objects;

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
                    if(entrance){
                        Span span = extractSpan(((BeforeEvent) event));
                        if(Objects.isNull(span)) {
                            Tracer.start(((BeforeEvent) event).invokeId, protocol);
                        }else {
                            Tracer.start(span.getTraceId(),protocol,((BeforeEvent) event).invokeId,null,span.getOverMachineUk());
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
        invocation.setInvokeId(event.invokeId);
        invocation.setStart(new Date().toInstant().toEpochMilli());
        invocation.setMethodName(event.javaMethodName);
        invocation.setClassName(event.javaClassName);
        InvocationCache.put(event.invokeId, invocation);
        assembleRequest(event,invocation);
    }

    public void doReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.get(event.invokeId);
        assembleResponse(event,invocation);
        invocation.setEnd(new Date().toInstant().toEpochMilli());

    }

    public void doThrow(ThrowsEvent event) {
        Invocation invocation = InvocationCache.get(event.invokeId);
        invocation.setThrowable(event.throwable);
        invocation.setThrowableMsg(event.throwable.getMessage());
        invocation.setThrowableClass(event.throwable.getClass().getCanonicalName());
        invocation.setEnd(new Date().toInstant().toEpochMilli());
    }

    public Invocation initInvocation(BeforeEvent event) {
        Invocation invocation = new Invocation();
        invocation.setInnerEntrance(entrance);
        invocation.setProtocol(protocol);
        invocation.setUk(Tracer.initUk(event.invokeId));
        invocation.setPreUk(entrance?Tracer.getOverMachineUk():Tracer.getPreUk(event.invokeId,protocol));
        invocation.setIp(Tracer.getLocalIp());
        invocation.setTraceId(Tracer.getTraceId());
        return invocation;
    }

    public void sendInvocationReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        toJson(invocation);
        InvocationSendUtil.send(invocation);
    }

    public void sendInvocationThrow(ThrowsEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        toJson(invocation);
        InvocationSendUtil.send(invocation);
    }


    public abstract void transportSpan(BeforeEvent event);
    public abstract Span extractSpan(BeforeEvent event);

    public abstract void assembleRequest(BeforeEvent event ,Invocation invocation);
    public  void assembleResponse(ReturnEvent event ,Invocation invocation){
        if(event.object!=null) {
            invocation.setResponse(event.object);
        }
    }

    public void toJson(Invocation invocation){
        invocation.setRequestJson(JSON.toJSONString(invocation.getRequest()));
        invocation.setResponseJson(JSON.toJSONString(invocation.getResponseJson()));
    }
}

