package me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.BasePlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo.DubboConsumerEventListener;

@Slf4j
public class JdbcPlugin extends BasePlugin {
    public JdbcPlugin(InvocationSendService invocationSendService) {
        super(Constant.JDBC,false,invocationSendService);
    }



    @Override
    public void watch(ModuleEventWatcher watcher) {
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
                .onWatch(new JdbcEventListener(entrance, protocol,invocationSendService), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
    }
}
