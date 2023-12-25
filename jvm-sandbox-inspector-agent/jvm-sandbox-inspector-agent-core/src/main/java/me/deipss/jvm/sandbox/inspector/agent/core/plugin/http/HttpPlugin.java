package me.deipss.jvm.sandbox.inspector.agent.core.plugin.http;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;

@Slf4j
public class HttpPlugin extends BasePlugin {
    public HttpPlugin() {
        super(Constant.HTTP,true);
        log.info("load plugin={},entrance={}",Constant.HTTP,false);
    }


    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("java.sql.Statement").includeBootstrap().includeSubClasses()
                .onBehavior("execute")
                .onWatch(new HttpEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
