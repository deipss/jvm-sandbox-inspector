package me.deipss.jvm.sandbox.inspector.agent.api.domain;


import java.io.Serializable;
import java.util.List;

public class GlobalConfig implements Serializable {
    private static final long serialVersionUID = -1L;

    /**
     * 心跳上报的URL
     */
    private String heartUrl;

    /**
     * 数据上报的URL
     */
    private String dataSendUrl;

    /**
     * 启用的插件
     */
    private List<String> enablePlugins;


}
