package me.deipss.jvm.sandbox.inspector.agent.core.plugin.concurrent;

import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;


@Slf4j
public class TtlConcurrentPlugin {
    public synchronized void watch(ModuleEventWatcher watcher) {
        log.info("load plugin={},entrance={}", "TtlConcurrentPlugin",false);
        new EventWatchBuilder(watcher)
                .onClass("java.util.concurrent.ThreadPoolExecutor").includeBootstrap()
                .onBehavior("execute")
                .onBehavior("submit")
                .onClass("java.util.concurrent.ScheduledThreadPoolExecutor").includeBootstrap()
                .onBehavior("execute")
                .onBehavior("submit")
                .onBehavior("schedule")
                .onBehavior("scheduleAtFixedRate")
                .onBehavior("scheduleWithFixedDelay")
                .onWatch(new TtlConcurrentListener());
    }
}
