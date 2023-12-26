package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class HttpEventListener extends BaseEventListener {
    public HttpEventListener(boolean entrance, String protocol) {
        super(entrance, protocol);
    }

    @Override
    public void transportSpan(BeforeEvent event) {
    }

    @Override
    public Span extractSpan(BeforeEvent event) {
        return null;
    }

    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        // void service(HttpServletRequest req, HttpServletResponse resp)
        HttpServletRequest req = (HttpServletRequest)event.argumentArray[0];
        HttpServletResponse resp = (HttpServletResponse)event.argumentArray[1];
        invocation.setUri(req.getRequestURI());
        invocation.setUrl(new String(req.getRequestURL()));

    }
}
