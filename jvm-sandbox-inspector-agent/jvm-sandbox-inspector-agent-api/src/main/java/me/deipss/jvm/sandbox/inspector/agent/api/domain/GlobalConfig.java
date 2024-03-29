package me.deipss.jvm.sandbox.inspector.agent.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
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
     * plugin jar 包的绝对路径
     */
    private String pluginsPath;

    /**
     * 启用的插件
     */
    private List<String> enablePlugins;

}
