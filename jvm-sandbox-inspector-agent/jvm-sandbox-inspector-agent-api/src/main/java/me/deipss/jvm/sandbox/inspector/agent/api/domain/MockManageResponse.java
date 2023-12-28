package me.deipss.jvm.sandbox.inspector.agent.api.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MockManageResponse {

    private Map<Integer,MockManageRequest> mockMap;
}
