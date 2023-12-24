package me.deipss.jvm.sandbox.inspector.agent.core.trace;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

@Slf4j
public class Tracer {

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class TraceContext {
        private String traceId;
        private String protocol;
        private int invokeId;
    }

    private static ThreadLocal<TraceContext> ttlContext = new TransmittableThreadLocal<>();


    private static String initTraceId(int invokeId) {
        return getLocalIp() + "_" + System.currentTimeMillis() + "_" + invokeId;
    }

    public static void start(int invokeId, String protocol) {
        String traceId = initTraceId(invokeId);
        start(traceId, protocol, invokeId);
    }

    public static void start(String traceId, String protocol, int invokeId) {
        ttlContext.set(new TraceContext(traceId, protocol, invokeId));
    }

    public static String get() {
        return ttlContext.get().getTraceId();
    }

    public static boolean nonNull() {
        return Objects.nonNull(ttlContext.get());
    }

    public static boolean end(String protocol, int invokeId) {
        TraceContext traceContext = ttlContext.get();
        if (Objects.isNull(traceContext)) {
            return true;
        }
        if (Objects.equals(traceContext.getProtocol(), protocol) && traceContext.getInvokeId() == invokeId) {
            ttlContext.remove();
            log.info("remove");
            return true;
        }
        return false;
    }


    public static String getLocalIp() {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "127.0.0.1";
        }
        return ip;
    }

}
