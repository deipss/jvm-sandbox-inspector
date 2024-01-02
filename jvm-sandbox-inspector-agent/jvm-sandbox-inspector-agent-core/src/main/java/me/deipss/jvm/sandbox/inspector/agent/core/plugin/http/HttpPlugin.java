package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

@Slf4j
public class HttpPlugin extends BasePlugin {
    public HttpPlugin(InvocationSendService invocationSendService) {
        super(Constant.HTTP,true,invocationSendService);
    }


    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("javax.servlet.http.HttpServlet").includeBootstrap().includeSubClasses()
                .onBehavior("service")
                .withParameterTypes("javax.servlet.http.HttpServletRequest", "javax.servlet.http.HttpServletResponse")
                .onWatch(new HttpEventListener(entrance, protocol,invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }


}
