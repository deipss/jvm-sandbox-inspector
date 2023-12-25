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

    public static final String SPAN="INSPECTOR_SPAN";

    private String traceId;

    private String overMachineUk;
}
