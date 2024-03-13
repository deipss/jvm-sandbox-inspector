package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class HttpResult {

    /**
     * 状态码
     * 200 - 请求成功
     * 301 - 资源（网页等）被永久转移到其它URL
     * 404 - 请求的资源（网页等）不存在
     * 500 - 内部服务器错误
     */
    private int httpStatus;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 返回的数据，一般是json格式
     */
    private String data;

}
