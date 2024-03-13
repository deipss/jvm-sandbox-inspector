package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockManageResponse implements Serializable {

    private static final long serialVersionUID = -1L;

    private Map<Integer,MockManageRequest.Inner> mockMap;
}
