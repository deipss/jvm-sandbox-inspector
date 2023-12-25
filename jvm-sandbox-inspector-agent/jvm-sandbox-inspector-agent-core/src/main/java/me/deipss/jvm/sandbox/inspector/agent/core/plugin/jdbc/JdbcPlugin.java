package me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo.DubboConsumerEventListener;

@Slf4j
public class JdbcPlugin extends BasePlugin {
    public JdbcPlugin() {
        super(Constant.JDBC,true);
        log.info("load plugin={},entrance={}",Constant.JDBC,false);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
        new EventWatchBuilder(watcher)
                .onClass("java.sql.Statement").includeBootstrap().includeSubClasses()
                .onBehavior("execute")
                .onClass("java.sql.PreparedStatement").includeBootstrap().includeSubClasses()
                .onBehavior("execute")
                .onWatch(new JdbcEventListener(entrance, protocol), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
