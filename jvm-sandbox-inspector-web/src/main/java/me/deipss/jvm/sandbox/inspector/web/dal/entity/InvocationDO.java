package me.deipss.jvm.sandbox.inspector.web.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * -- auto Generated on 2024-01-02
 * -- DROP TABLE IF EXISTS t_invocation;
 * CREATE TABLE t_invocation(
 *   id BIGINT (15) NOT NULL AUTO_INCREMENT COMMENT 'id',
 *   protocol VARCHAR (32) NOT NULL DEFAULT '' COMMENT '协议',
 *   traceId VARCHAR (64) NOT NULL DEFAULT '' COMMENT '链路ID',
 *   uk VARCHAR (64) NOT NULL DEFAULT '' COMMENT '唯一标识',
 *   preUk VARCHAR (64) NOT NULL DEFAULT '' COMMENT '父调用 唯一标识',
 *   invokeId INT (8) NOT NULL DEFAULT -1 COMMENT '@see com.alibaba.jvm.sandbox.api.event.InvokeEvent#invokeId',
 *   innerEntrance TINYINT (1) NOT NULL DEFAULT 0 COMMENT '是否为当前机器内部调用的入口',
 *   outerEntrance TINYINT (1) NOT NULL DEFAULT 0 COMMENT '是否为跳跃多台机器，调用的入口',
 *   requestJson LONGTEXT NOT NULL  COMMENT '请求参数',
 *   className VARCHAR (256) NOT NULL DEFAULT '' COMMENT '类名',
 *   methodName VARCHAR (128) NOT NULL DEFAULT '' COMMENT '方法名',
 *   rpcContext VARCHAR (1024) NOT NULL DEFAULT '' COMMENT 'dubbo rpc context',
 *   responseJson LONGTEXT NOT NULL  COMMENT '返回结果',
 *   throwableMsg VARCHAR (256) NOT NULL DEFAULT '' COMMENT '异常信息',
 *   throwableClass VARCHAR (256) NOT NULL DEFAULT '' COMMENT '异常类型',
 *   `sql` TEXT NOT NULL COMMENT 'sql语句',
 *   topic VARCHAR (256) NOT NULL DEFAULT '' COMMENT '消息队列的主题',
 *   msgId VARCHAR (64) NOT NULL DEFAULT '' COMMENT '消息队列的消息ID',
 *   tags VARCHAR (32) NOT NULL DEFAULT '' COMMENT '消息队列的标签',
 *   mqBody TEXT NOT NULL COMMENT '消息体',
 *   `start` BIGINT (15) NOT NULL DEFAULT -1 COMMENT '调用开始时间',
 *   `end` BIGINT (15) NOT NULL DEFAULT -1 COMMENT '调用结束时间',
 *   `endTime` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '调用结束时间',
 *   `startTime` DATETIME NOT NULL DEFAULT '1000-01-01 00:00:00' COMMENT '调用结束时间',
 *   ip VARCHAR (5016) NOT NULL DEFAULT '' COMMENT 'ip',
 *   httpPort INT (8) NOT NULL DEFAULT -1 COMMENT 'httpPort',
 *   uri VARCHAR (128) NOT NULL DEFAULT '' COMMENT 'http uri',
 *   url VARCHAR (256) NOT NULL DEFAULT '' COMMENT 'http url',
 *   contentType VARCHAR (64) NOT NULL DEFAULT '' COMMENT 'http contentType',
 *   httpBody TEXT NOT NULL COMMENT 'http body',
 *   httpHeaders VARCHAR (256) NOT NULL DEFAULT '' COMMENT 'http headers',
 *   httpParameters VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'http Parameters',
 *   PRIMARY KEY (id)
 * )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 't_invocation';
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("t_invocation")

public class InvocationDO implements Serializable {
    private static final long serialVersionUID = -1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 协议
     */
    @TableField("protocol")
    private String protocol;

    /**
     * 链路ID
     */
    @TableField("traceId")
    private String traceId;

    /**
     * 唯一标识
     */
    @TableField("uk")
    private String uk;

    /**
     * 父调用 唯一标识
     */
    @TableField("preUk")
    private String preUk;

    /**
     * @see com.alibaba.jvm.sandbox.api.event.InvokeEvent#invokeId
     */
    @TableField("invokeId")
    private Integer invokeId;

    /**
     * 是否为当前机器内部调用的入口
     */
    @TableField("innerEntrance")
    private Boolean innerEntrance;

    /**
     * 是否为跳跃多台机器，调用的入口
     */
    @TableField("outerEntrance")
    private Boolean outerEntrance;

    /**
     * 请求参数
     */
    @TableField("requestJson")
    private String requestJson;


    /**
     * 类名
     */
    @TableField("className")
    private String className;

    /**
     * 方法名
     */
    @TableField("methodName")
    private String methodName;

    /**
     * dubbo rpc context
     */
    @TableField("rpcContext")
    private String rpcContext;

    /**
     * 返回结果
     */
    @TableField("responseJson")
    private String responseJson;

    /**
     * 异常信息
     */
    @TableField("throwableMsg")
    private String throwableMsg;

    /**
     * 异常类型
     */
    @TableField("throwableClass")
    private String throwableClass;

    /**
     * sql语句
     */
    @TableField("sql")
    private String sql;

    /**
     * 消息队列的主题
     */
    @TableField("topic")
    private String topic;

    /**
     * 消息队列的消息ID
     */
    @TableField("msgId")
    private String msgId;

    /**
     * 消息队列的标签
     */
    @TableField("tags")
    private String tags;


    /**
     * 消息体
     */
    @TableField("mqBody")
    private String mqBody;


    /**
     * 调用开始时间
     */
    @TableField("start")
    private Long start;

    /**
     * 调用开始时间
     */
    @TableField("startTime")
    private Date startTime;

    /**
     * 调用结束时间
     */
    @TableField("end")
    private Long end;

    /**
     * 调用结束时间
     */
    @TableField("endTime")
    private Date endTime;

    /**
     * ip
     */
    @TableField("ip")
    private String ip;

    @TableField("httpPort")
    private Integer httpPort;

    /**
     * http uri
     */
    @TableField("uri")
    private String uri;

    /**
     * http url
     */
    @TableField("url")
    private String url;


    /**
     * http contentType
     */
    @TableField("contentType")
    private String contentType;

    /**
     * http body
     */
    @TableField("httpBody")
    private String httpBody;


    /**
     * http headers
     */
    @TableField("httpHeaders")
    private String httpHeaders;

    /**
     * http Parameters
     */
    @TableField("httpParameters")
    private String httpParameters;


}
