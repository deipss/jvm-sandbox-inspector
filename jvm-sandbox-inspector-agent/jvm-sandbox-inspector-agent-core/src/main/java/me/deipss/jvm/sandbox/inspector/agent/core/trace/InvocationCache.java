package me.deipss.jvm.sandbox.inspector.agent.core.trace;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.Invocation;

import java.util.concurrent.TimeUnit;

public class InvocationCache {

    private static final LoadingCache<Integer, Invocation> innerMap = CacheBuilder
            .newBuilder()
            .maximumSize(1<<12)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Integer, Invocation>() {
                @Override
                public Invocation load(Integer key) throws Exception {
                    return new Invocation();
                }
            });

    public static Invocation get(int invokeId) {
        return innerMap.getIfPresent(invokeId);
    }

    public static Invocation remove(int invokeId) {
        Invocation invocation = innerMap.getIfPresent(invokeId);
        innerMap.invalidate(invokeId);
        return invocation;
    }

    public static void put(int invokeId, Invocation invocation) {
        innerMap.put(invokeId, invocation);
    }
}
