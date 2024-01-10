package me.deipss.jvm.sandbox.inspector.agent.api.service;

import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageResponse;

import java.util.List;

public interface MockManageService {




    int stop(int mockId);

    int addAll(List<MockManageRequest.Inner> request);

    MockManageResponse view();




}
