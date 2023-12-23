package me.deipss.jvm.sandbox.inspector.agent.core.plugin.concurrent;

import com.alibaba.jvm.sandbox.api.event.Event.Type;
import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceAdapterListener;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;

import java.util.concurrent.Callable;


public class TtlConcurrentAdvice {

    private final ModuleEventWatcher watcher;

    private TtlConcurrentAdvice(ModuleEventWatcher watcher) {
        this.watcher = watcher;
    }

    public static TtlConcurrentAdvice watcher(ModuleEventWatcher watcher) {
        return new TtlConcurrentAdvice(watcher);
    }

    public synchronized void watch() {
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
                .onWatch(new AdviceAdapterListener(new AdviceListener() {
                    @Override
                    protected void before(Advice advice) throws Throwable {
                        // 包装成ttl
                        final Object[] parameterArray = advice.getParameterArray();
                        final Class<?>[] parameterTypeArray = advice.getBehavior().getParameterTypes();
                        if (parameterArray == null || parameterArray.length < 1) {return;}
                        if (parameterArray[0] instanceof com.alibaba.ttl.TtlEnhanced) {return;}
                        Class<?> parameter0Type = parameterTypeArray[0];
                        if (parameter0Type.isAssignableFrom(Runnable.class)) {
                            parameterArray[0] = TtlRunnable.get((Runnable) parameterArray[0]);
                        }
                        if (parameter0Type.isAssignableFrom(Callable.class)) {
                            parameterArray[0] = TtlCallable.get((Callable) parameterArray[0]);
                        }
                    }
                    // fix issue #41
                }), Type.BEFORE, Type.RETURN, Type.THROWS);
    }
}
