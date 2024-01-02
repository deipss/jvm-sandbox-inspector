package me.deipss.jvm.sandbox.inspector.web.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * -- auto Generated on 2024-01-02
 * -- DROP TABLE IF EXISTS t_heart_beat;
 * CREATE TABLE t_heart_beat(
 *   id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT 'id',
 *   app VARCHAR (50) NOT NULL DEFAULT '' COMMENT '应用名称',
 *   ip VARCHAR (16) NOT NULL DEFAULT '' COMMENT '容器IP',
 *   version VARCHAR (16) NOT NULL DEFAULT '' COMMENT '当前版本号',
 *   beatTime DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '心跳时间',
 *   envTag VARCHAR (8) NOT NULL DEFAULT '' COMMENT '环境标识',
 *   port VARCHAR (8) NOT NULL DEFAULT '' COMMENT '端口',
 *   moduleStatus VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'moduleManager.isActivated(Constants.MODULE_ID) ? "ACTIVE" : "FROZEN"',
 *   PRIMARY KEY (id)
 * )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 't_heart_beat';
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_heart_beat")

public class HeartBeatDO implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    @TableField("app")
    private String app;

    /**
     * 容器IP
     */
    @TableField("ip")
    private String ip;


    /**
     * 当前版本号
     */
    @TableField("version")
    private String version;

    /**
     * 心跳时间
     */
    @TableField("beatTime")
    private Date beatTime;

    /**
     * 环境标识
     */
    @TableField("envTag")
    private String envTag;

    /**
     * 端口
     */
    @TableField("port")
    private String port;

    /**
     * moduleManager.isActivated(Constants.MODULE_ID) ? "ACTIVE" : "FROZEN"
     */
    @TableField("moduleStatus")
    private String moduleStatus;

}
