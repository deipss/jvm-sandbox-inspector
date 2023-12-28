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
        private String preUk;
        private String overMachineUk;
    }

    private static ThreadLocal<TraceContext> ttlContext = new TransmittableThreadLocal<>();


    public static String initUk(int invokeId) {
        return getLocalIp() + "_" + System.currentTimeMillis() + "_" + invokeId;
    }

    public static void start(int invokeId, String protocol) {
        String uk = initUk(invokeId);
        start(uk, protocol, invokeId,uk,null);
    }

    public static void start(String traceId, String protocol, int invokeId,String preUk,String overMachineUk) {
        ttlContext.set(new TraceContext(traceId, protocol, invokeId,preUk,overMachineUk));
    }

    public static String getTraceId() {
        return ttlContext.get().getTraceId();
    }

    public static String getPreUk(int invokeId, String protocol) {
        TraceContext traceContext = ttlContext.get();
        if(Objects.isNull(traceContext)){
            start(invokeId,protocol);
            log.warn("trace is null ,start now");
        }
        return ttlContext.get().getPreUk();
    }

    public static String getOverMachineUk() {
        return ttlContext.get().getOverMachineUk();
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
            log.error("获取IP地址失败,返回 127.0.0.1",e);
            ip = "127.0.0.1";
        }
        return ip;
    }

}
