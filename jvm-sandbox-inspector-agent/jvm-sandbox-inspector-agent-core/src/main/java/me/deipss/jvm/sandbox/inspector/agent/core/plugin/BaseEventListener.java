package me.deipss.jvm.sandbox.inspector.agent.core.plugin;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.event.ReturnEvent;
import com.alibaba.jvm.sandbox.api.event.ThrowsEvent;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;

public abstract class BaseEventListener implements EventListener {
    @Override
    public void onEvent(Event event) throws Throwable {
        switch (event.type) {
            case BEFORE:
                doBefore((BeforeEvent) event);
                break;
            case RETURN:
                doReturn((ReturnEvent) event);
                break;
            case THROWS:
                doThrow((ThrowsEvent) event);
                break;
            default:
                break;
        }
    }

    abstract public void doBefore(BeforeEvent event);
    abstract public void doReturn(ReturnEvent event);
    abstract public void doThrow(ThrowsEvent event);
    abstract public Invocation initInvocation(BeforeEvent event);
}

