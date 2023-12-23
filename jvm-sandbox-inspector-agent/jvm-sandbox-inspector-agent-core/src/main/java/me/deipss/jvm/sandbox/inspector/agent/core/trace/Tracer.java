package me.deipss.jvm.sandbox.inspector.agent.core.trace;

import com.alibaba.ttl.TransmittableThreadLocal;

public class Tracer {
    private static ThreadLocal<String> ttlContext = new TransmittableThreadLocal<>();

}
