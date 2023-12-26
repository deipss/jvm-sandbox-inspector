package me.deipss.jvm.sandbox.inspector.agent.api.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Span implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 魔法数，在http请求头、dubbo上下文、rocket user property中传递
     */
    public static final String SPAN="INSPECTOR_SPAN";

    /**
     * 链路ID
     */

    private String traceId;

    /**
     * 跨越机器的
     * 调用标识
     *
     */
    private String overMachineUk;
}
