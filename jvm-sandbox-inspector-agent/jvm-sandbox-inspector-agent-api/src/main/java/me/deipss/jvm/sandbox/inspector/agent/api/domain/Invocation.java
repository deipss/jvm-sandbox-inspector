package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Invocation implements Serializable {
    private static final long serialVersionUID = -1L;

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
     * 是否为当前机器内部调用的入口
     */
    private boolean innerEntrance;

    /**
     * 是否为跳跃多台机器，调用的入口
     */
    private boolean outerEntrance;

    /**
     * 请求参数
     */
    private String requestJson;


    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * dubbo rpc context
     */
    private Map<String, String> rpcContext;

    /**
     * 返回结果
     */
    private String responseJson;

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
     * 消息队列的消息ID
     */
    private String msgId;

    /**
     * 消息队列的标签
     */
    private String tags;


    /**
     * 消息体
     */
    private String mqBody;


    /**
     * 调用开始时间
     */
    private Long start;

    /**
     * 调用结束时间
     */
    private Long end;

    /**
     * ip
     */
    private String ip;


    private int httpPort;

    /**
     * http uri
     */
    private String uri;

    /**
     * http url
     */
    private String url;


    /**
     * http contentType
     */
    private String contentType;

    /**
     * http body
     */
    private String httpBody;


    /**
     * http headers
     */
    private Map<String, String> httpHeaders;

    /**
     * http Parameters
     */
    private Map<String, String[]> httpParameters;


    private transient Object[] request;

    private transient Object response;

    private transient Throwable throwable;



}
