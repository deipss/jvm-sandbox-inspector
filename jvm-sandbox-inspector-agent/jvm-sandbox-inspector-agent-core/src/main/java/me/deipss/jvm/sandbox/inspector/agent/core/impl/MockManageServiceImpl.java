package me.deipss.jvm.sandbox.inspector.agent.core.impl;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageResponse;
import me.deipss.jvm.sandbox.inspector.agent.api.service.MockManageService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.mock.MockEventListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MockManageServiceImpl implements MockManageService {

    private ConcurrentHashMap<Integer,MockManageRequest.Inner> mockMap;

    private ModuleEventWatcher moduleEventWatcher;

    public MockManageServiceImpl(ModuleEventWatcher moduleEventWatcher) {
        this.moduleEventWatcher = moduleEventWatcher;
        mockMap = new ConcurrentHashMap<>(8);
    }

    @Override
    public int stop(int mockId) {
        moduleEventWatcher.delete(mockId);
        return 1;
    }

    public int add(MockManageRequest.Inner request) {

        EventWatchBuilder.IBuildingForClass iBuildingForClass = new EventWatchBuilder(moduleEventWatcher)
                .onClass(request.getMockClass()).includeBootstrap();
        if(request.isIncludeSubClass()) {
            iBuildingForClass
                    .includeSubClasses();
        }
        EventWatchBuilder.IBuildingForBehavior iBuildingForBehavior = iBuildingForClass.onBehavior(request.getMockMethod());
        if(null!=request.getParameterTypes()) {
            iBuildingForBehavior.withParameterTypes(request.getParameterTypes());

        }
        iBuildingForBehavior.onWatch(new MockEventListener(request){

        }, Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);


        return 1;
    }

    @Override
    public int addAll(List<MockManageRequest.Inner> list) {
        int result = 0;
        for (MockManageRequest.Inner mockManageRequest : list) {
            add(mockManageRequest);
            ++result;
        }
        return result;
    }

    @Override
    public MockManageResponse view() {
        MockManageResponse mockManageResponse = new MockManageResponse();
        mockManageResponse.setMockMap(mockMap);
        return mockManageResponse;
    }


}
