package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartBeat {

    /**
     * 应用名称
     */
    private String app;

    /**
     * 容器IP
     */
    private String ip;

    /**
     * 当前版本号
     */
    private int intVersion;

    /**
     * 当前版本号
     */
    private String version;

    /**
     * 心跳时间
     */
    private Date beatTime;

    /**
     * 环境标识
     */
    private String envTag;

    /**
     * 端口
     */
    private String port;

    /**
     * moduleManager.isActivated(Constants.MODULE_ID) ? "ACTIVE" : "FROZEN"
     */
    private String moduleStatus;

}
