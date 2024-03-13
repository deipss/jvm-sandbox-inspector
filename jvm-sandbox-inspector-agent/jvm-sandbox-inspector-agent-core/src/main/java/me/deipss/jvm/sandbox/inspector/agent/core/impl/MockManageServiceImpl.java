package me.deipss.jvm.sandbox.inspector.agent.core.impl;

import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatchBuilder;
import com.alibaba.jvm.sandbox.api.listener.ext.EventWatcher;
import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageResponse;
import me.deipss.jvm.sandbox.inspector.agent.api.service.MockManageService;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.mock.MockEventListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MockManageServiceImpl implements MockManageService {

    private final ConcurrentHashMap<Integer , MockManageRequest.Inner> mockMap;

    private final ModuleEventWatcher moduleEventWatcher;

    private final LoadedClassDataSource loadedClassDataSource;

    public MockManageServiceImpl(ModuleEventWatcher moduleEventWatcher,LoadedClassDataSource loadedClassDataSource) {
        this.moduleEventWatcher = moduleEventWatcher;
        this.loadedClassDataSource = loadedClassDataSource;
        mockMap = new ConcurrentHashMap<>(8);
    }


    @Override
    public int stop(Integer mockId) {
        if (mockMap.containsKey(mockId)) {
            moduleEventWatcher.delete(mockId);
            mockMap.remove(mockId);
            log.info("delete mock id ={}", mockId);
        } else {
            log.info("mockMap not contain {}", mockId);

        }
        return 1;
    }

    public int add(MockManageRequest.Inner request) {

        EventWatchBuilder.IBuildingForClass iBuildingForClass = new EventWatchBuilder(moduleEventWatcher)
                .onClass(request.getMockClass()).includeBootstrap();
        if (request.isIncludeSubClass()) {
            iBuildingForClass.includeSubClasses();
        }
        EventWatchBuilder.IBuildingForBehavior iBuildingForBehavior = iBuildingForClass.onBehavior(request.getMockMethod());
        if (null != request.getParameterTypes()) {
            iBuildingForBehavior.withParameterTypes(request.getParameterTypes());
        }
        EventWatcher eventWatcher = iBuildingForBehavior.onWatch(new MockEventListener(request,loadedClassDataSource), Event.Type.BEFORE, Event.Type.RETURN, Event.Type.THROWS);
        mockMap.put(eventWatcher.getWatchId(), request);
        log.info("add mock ,watchId={}", eventWatcher.getWatchId());
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
