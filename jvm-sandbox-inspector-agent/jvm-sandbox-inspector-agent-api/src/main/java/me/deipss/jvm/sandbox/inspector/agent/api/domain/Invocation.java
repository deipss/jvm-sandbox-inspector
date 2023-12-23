package me.deipss.jvm.sandbox.inspector.agent.api.domain;

public class Invocation {

    /**
     * 协议
     */
    private String protocol;

    /**
     * 链路ID
     */
    private String traceId;

    /**
     * 唯一标识
     */
    private String uk;

    /**
     * 父调用 唯一标识
     */
    private String preUk;
    /**
     * @see com.alibaba.jvm.sandbox.api.event.InvokeEvent#invokeId
     */
    private int invokeId;
    /**
     * @see com.alibaba.jvm.sandbox.api.event.InvokeEvent#processId
     */
    private int processId;

    /**
     * 内部计数
     */
    private Integer index;

    /**
     * 是否为当前机器内部调用的入口
     */
    private boolean innerEntrace;

    /**
     * 是否为跳跃多台机器，调用的入口
     */
    private boolean outerEntrace;

    /**
     * 请求参数
     */
    private String requestJson;

    /**
     * 返回结果
     */
    private String reponseJson;

    /**
     * 异常信息
     */
    private String throwableMsg;

    /**
     * 异常类型
     */
    private String throwableClass;

    /**
     * sql语句
     */
    private String sql;

    /**
     * 消息队列的主题
     */
    private String topic;


    /**
     * 调用开始时间
     */
    private Long start;

    /**
     * 调用结束时间
     */
    private Long end;

    /**
     * 类加载器
     */
    private transient ClassLoader classLoader;


    private transient Object[] request;

    private transient Object response;

    private transient Throwable throwable;


}
