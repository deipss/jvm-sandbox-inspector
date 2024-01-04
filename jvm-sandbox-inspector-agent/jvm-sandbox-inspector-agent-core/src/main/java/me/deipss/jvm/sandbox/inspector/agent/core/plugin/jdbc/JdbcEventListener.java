package me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc;

import com.alibaba.jvm.sandbox.api.event.BeforeEvent;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Span;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BaseEventListener;

@Slf4j
public class JdbcEventListener extends BaseEventListener {
    public JdbcEventListener(boolean entrance, String protocol, InvocationSendService invocationSendService) {
        super(entrance, protocol, invocationSendService);
    }


    @Override
    public void assembleRequest(BeforeEvent event, Invocation invocation) {
        if (null != event.argumentArray && event.argumentArray.length > 0) {
            invocation.setSql(event.argumentArray[0].toString());
        }
    }
}
