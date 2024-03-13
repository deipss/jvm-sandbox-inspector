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

    /**
     * 链路上下文信息
     * 关键就是链路何时清除，何时新建
     */
    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class TraceContext {

        /**
         * 链路ID
         */
        private String traceId;

        /**
         * 链路入口的协议
         */
        private String protocol;

        /**
         * 调用ID
         */
        private int invokeId;

        /**
         * 上一级调用
         */
        private String preUk;

        /**
         * 当前的UK
         */
        private String overMachineUk;
    }

    private static final ThreadLocal<TraceContext> ttlContext = new TransmittableThreadLocal<>();


    public static String initUk(int invokeId) {
        return getLocalIp() + "_" + System.currentTimeMillis() + "_" + invokeId;
    }

    /**
     * 一条新链路的开始
     */
    public static void start(int invokeId, String protocol) {
        String uk = initUk(invokeId);
        start(uk, protocol, invokeId, uk, null);
    }

    /**
     * 已经从上一台机器传递了跨越jvm的链路
     */
    public static void start(String traceId, String protocol, int invokeId, String preUk, String overMachineUk) {
        ttlContext.set(new TraceContext(traceId, protocol, invokeId, preUk, overMachineUk));
    }

    public static String getTraceId() {
        return ttlContext.get().getTraceId();
    }

    public static String getPreUk(int invokeId, String protocol) {
        TraceContext traceContext = ttlContext.get();
        if (Objects.isNull(traceContext)) {
            start(invokeId, protocol);
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

    public static void end(String protocol, int invokeId) {
        TraceContext traceContext = ttlContext.get();
        if (Objects.isNull(traceContext)) {
            log.error("current trace context is null,return now ");
            return;
        }
        if (Objects.equals(traceContext.getProtocol(), protocol) && traceContext.getInvokeId() == invokeId) {
            ttlContext.remove();
            log.info("remove");
        } else {
            log.error("current protocol={},invokeId={},while trace context protocol={},invokeId={} ", protocol, invokeId, traceContext.getProtocol(), traceContext.getInvokeId());
        }
    }


    public static String getLocalIp() {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取IP地址失败,返回 127.0.0.1", e);
            ip = "127.0.0.1";
        }
        return ip;
    }

}
