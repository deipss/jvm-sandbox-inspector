package me.deipss.jvm.sandbox.inspector.agent.api.service;

import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;

public interface InvocationSendService {
    void send(Invocation invocation);
}
