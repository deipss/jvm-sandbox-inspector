package me.deipss.jvm.sandbox.inspector.web.service.impl;

import me.deipss.jvm.sandbox.inspector.agent.api.domain.HeartBeat;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;
import me.deipss.jvm.sandbox.inspector.web.domain.page.PageResponse;

public interface InspectorService {

    void heartBeat(HeartBeat heartBeat);


    void dataSync(Invocation invocation);


    PageResponse<?> page();
}
