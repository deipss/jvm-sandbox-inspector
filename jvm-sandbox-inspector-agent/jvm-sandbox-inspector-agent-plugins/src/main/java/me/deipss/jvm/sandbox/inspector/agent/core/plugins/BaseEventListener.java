package me.deipss.jvm.sandbox.inspector.agent.core.plugins;

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
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.InvocationCache;
import me.deipss.jvm.sandbox.inspector.agent.core.trace.Tracer;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public abstract class BaseEventListener implements EventListener {

    protected boolean entrance;

    protected String protocol;

    protected InvocationSendService invocationSend;

    @Override
    public void onEvent(Event event) throws Throwable {
        try {
            switch (event.type) {
                case BEFORE:
                    Span span = null;
                    if (entrance) {
                         span = extractSpan(((BeforeEvent) event));
                        if (Objects.isNull(span)) {
                            Tracer.start(((BeforeEvent) event).invokeId, protocol);
                        } else {
                            log.info("span ={}",span );
                            Tracer.start(span.getTraceId(), protocol, ((BeforeEvent) event).invokeId, null, span.getOverMachineUk());
                        }
                    }
                    doBefore((BeforeEvent) event,span);
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

    public void doBefore(BeforeEvent event,Span span) {
        Invocation invocation = initInvocation(event);
        invocation.setOuterEntrance(outerEntrance(span));
        InvocationCache.put(event.invokeId, invocation);
        assembleRequest(event, invocation);
    }

    public void doReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.get(event.invokeId);
        if (invocation == null) {
            log.error("doReturn invocation is null,class ={}", this.getClass().getCanonicalName());
            return;
        }
        assembleResponse(event, invocation);
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
        doInitInvocation(event, invocation);
        return invocation;
    }

    protected void doInitInvocation(BeforeEvent event, Invocation invocation) {
        invocation.setInnerEntrance(entrance);
        invocation.setInvokeId(event.invokeId);
        invocation.setStart(new Date().toInstant().toEpochMilli());
        invocation.setMethodName(event.javaMethodName);
        invocation.setClassName(event.javaClassName);
        invocation.setProtocol(protocol);
        invocation.setUk(Tracer.initUk(event.invokeId));
        invocation.setPreUk(entrance ? Tracer.getOverMachineUk() : Tracer.getPreUk(event.invokeId, protocol));
        invocation.setIp(Tracer.getLocalIp());
        invocation.setTraceId(Tracer.getTraceId());
    }

    public void sendInvocationReturn(ReturnEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        toJson(invocation);
        invocationSend.send(invocation);
    }

    public void sendInvocationThrow(ThrowsEvent event) {
        Invocation invocation = InvocationCache.remove(event.invokeId);
        toJson(invocation);
        invocationSend.send(invocation);
    }


    public  void transportSpan(BeforeEvent event){

    }

    public  Span extractSpan(BeforeEvent event){
        return null;
    }

    public abstract void assembleRequest(BeforeEvent event, Invocation invocation);

    public void assembleResponse(ReturnEvent event, Invocation invocation) {
        if (event.object != null) {
            invocation.setResponse(event.object);
        }
    }

    public void toJson(Invocation invocation) {
        invocation.setRequestJson(JSON.toJSONString(invocation.getRequest()));
        invocation.setResponseJson(JSON.toJSONString(invocation.getResponse()));
    }

    private boolean outerEntrance(Span span){
        return entrance && span==null;
    }
}

