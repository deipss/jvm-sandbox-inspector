package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.MockType;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.Operate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockManageRequest {

    private MockType mockType;
    private Operate operate;

    private String mockResponseJson;

    private Integer mockId;
    private String mockClass;
    private String mockMethod;
    private boolean includeSubClass;
    private String [] parameterTypes;
    private String exceptionClassCanonicalName;
    private String returnClassCanonicalName;
}
