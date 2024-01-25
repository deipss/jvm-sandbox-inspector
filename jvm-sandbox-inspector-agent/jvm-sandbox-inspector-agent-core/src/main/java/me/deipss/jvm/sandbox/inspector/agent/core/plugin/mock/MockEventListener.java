package me.deipss.jvm.sandbox.inspector.agent.core.plugin.mock;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.filter.Filter;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.MockType;

import java.util.Set;

import static com.alibaba.jvm.sandbox.api.ProcessController.returnImmediately;
import static com.alibaba.jvm.sandbox.api.ProcessController.throwsImmediately;

@Slf4j
public class MockEventListener implements EventListener {

    private MockManageRequest.Inner mockManageRequest;

    private LoadedClassDataSource loadedClassDataSource;

    public MockEventListener(MockManageRequest.Inner mockManageRequest ,LoadedClassDataSource loadedClassDataSource) {
        this.mockManageRequest = mockManageRequest;
        this.loadedClassDataSource = loadedClassDataSource;
    }

    @Override
    public void onEvent(Event event) throws Throwable {
        if (event.type.equals(Event.Type.BEFORE)) {
            if (mockManageRequest.getMockType().equals(MockType.THROW_BEFORE_EXE)) {
                Class<?> throwClass = Class.forName(mockManageRequest.getExceptionClassCanonicalName());
                log.info("throwsImmediately class= {}",mockManageRequest.getExceptionClassCanonicalName() );
                throwsImmediately((Throwable) throwClass.newInstance());
            }

            if (mockManageRequest.getMockType().equals(MockType.RETURN_BEFORE_EXE)) {
                Set<Class<?>> classes = loadedClassDataSource.find(new MockClassFilter(mockManageRequest.getReturnClassCanonicalName()));
                if(!classes.isEmpty()) {
                    Class<?> next = classes.iterator().next();
                    Object o = JSON.parseObject(mockManageRequest.getMockResponseJson(), next);
                    log.info("returnImmediately class= {}", mockManageRequest.getReturnClassCanonicalName());
                    returnImmediately(o);
                }else{
                    log.info("returnImmediately failed ,not found  class= {}", mockManageRequest.getReturnClassCanonicalName());
                }
            }
        }
    }
}
