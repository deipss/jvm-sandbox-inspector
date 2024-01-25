package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.MockType;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.Operate;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockManageRequest implements Serializable {
    private static final long serialVersionUID = -1L;

    private Operate operate;

    private List<Inner> mockInnerList;



    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Inner implements Serializable {
        private static final long serialVersionUID = -1L;

        private MockType mockType;
        private String mockResponseJson;
        private Integer mockId;
        private String mockClass;
        private String mockMethod;
        private boolean includeSubClass;
        private String [] parameterTypes;
        private String exceptionClassCanonicalName;
        private String returnClassCanonicalName;

    }
}
