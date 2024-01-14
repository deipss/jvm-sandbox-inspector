package me.deipss.jvm.sandbox.inspector.agent.core.plugins.http;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.api.spi.InspectorPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugins.BasePlugin;
import org.kohsuke.MetaInfServices;

@Slf4j
@MetaInfServices(InspectorPlugin.class)

public class HttpPlugin extends BasePlugin {



    @Override
    public String identify() {
        return Constant.HTTP;
    }

    @Override
    public boolean entrance() {
        return true;
    }

    @Override
    public int watch(ModuleEventWatcher watcher, InvocationSendService invocationSendService) {
        new EventWatchBuilder(watcher)
                .onClass("javax.servlet.http.HttpServlet").includeBootstrap().includeSubClasses()
                .onBehavior("service")
                .withParameterTypes("javax.servlet.http.HttpServletRequest", "javax.servlet.http.HttpServletResponse")
                .onWatch(new HttpEventListener(entrance(), identify(),invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);

        return 1;
    }
}
