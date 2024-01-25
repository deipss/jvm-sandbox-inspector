package me.deipss.jvm.sandbox.inspector.agent.api.spi;

import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;

import java.util.List;

public interface InspectorPlugin {

    String identify();

    boolean enable(List<String> plugins);

    boolean entrance();

    int watch(ModuleEventWatcher watcher,InvocationSendService invocationSendService);


}
