package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import com.google.common.base.Strings;
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
        try {
            HttpServletRequest req = (HttpServletRequest)event.argumentArray[0];
            String header = req.getHeader(Span.SPAN);
            if(Strings.isNullOrEmpty(header)) {
                return null;
            }
            return JSON.parseObject(header,Span.class);
        } catch (Exception e) {
            log.info("http extract span ,error",e);
        }
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
