package me.deipss.jvm.sandbox.inspector.agent.core.plugin.concurrent;

import com.alibaba.jvm.sandbox.api.listener.ext.Advice;
import com.alibaba.jvm.sandbox.api.listener.ext.AdviceListener;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;

@Slf4j
public class TtlConcurrentListener extends AdviceListener{
    @Override
    protected void before(Advice advice) throws Throwable {
        // 包装成ttl
        final Object[] parameterArray = advice.getParameterArray();
        final Class<?>[] parameterTypeArray = advice.getBehavior().getParameterTypes();
        if (parameterArray == null || parameterArray.length < 1) {
            return;
        }
        if (parameterArray[0] instanceof com.alibaba.ttl.TtlEnhanced) {
            return;
        }
        Class<?> parameter0Type = parameterTypeArray[0];
        if (parameter0Type.isAssignableFrom(Runnable.class)) {
            parameterArray[0] = TtlRunnable.get((Runnable) parameterArray[0]);
        }
        if (parameter0Type.isAssignableFrom(Callable.class)) {
            parameterArray[0] = TtlCallable.get((Callable) parameterArray[0]);
        }
//        log.info("ttl wrapper success");
    }
}
