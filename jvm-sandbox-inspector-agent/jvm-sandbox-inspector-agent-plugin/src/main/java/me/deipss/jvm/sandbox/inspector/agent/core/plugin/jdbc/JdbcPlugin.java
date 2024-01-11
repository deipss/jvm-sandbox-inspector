package me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.api.spi.InspectorPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;
import org.kohsuke.MetaInfServices;

@Slf4j
@MetaInfServices(InspectorPlugin.class)
public class JdbcPlugin extends BasePlugin {
    @Override
    public String identify() {
        return Constant.JDBC;
    }

    @Override
    public boolean entrance() {
        return false;
    }

    @Override
    public int watch(ModuleEventWatcher watcher, InvocationSendService invocationSendService) {
        new EventWatchBuilder(watcher)
                .onClass("java.sql.Statement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onClass("com.mysql.jdbc.PreparedStatement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onClass("com.mysql.jdbc.ServerPreparedStatement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onClass("com.mysql.cj.jdbc.PreparedStatement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onClass("com.mysql.cj.jdbc.ClientPreparedStatement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onClass("com.mysql.cj.jdbc.ServerPreparedStatement")
                .onBehavior("execute")
                .onBehavior("executeQuery")
                .onBehavior("executeUpdate")
                .onWatch(new JdbcEventListener(entrance(), identify(), invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
        return 1;
    }
}
