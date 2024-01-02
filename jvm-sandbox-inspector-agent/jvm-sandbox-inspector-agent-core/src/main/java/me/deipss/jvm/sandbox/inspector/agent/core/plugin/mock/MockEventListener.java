package me.deipss.jvm.sandbox.inspector.agent.core.plugin.mock;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.event.Event;
import com.alibaba.jvm.sandbox.api.listener.EventListener;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.enums.MockType;

import static com.alibaba.jvm.sandbox.api.ProcessController.returnImmediately;
import static com.alibaba.jvm.sandbox.api.ProcessController.throwsImmediately;

@Slf4j
public class MockEventListener implements EventListener {

    private MockManageRequest mockManageRequest;

    public MockEventListener(MockManageRequest mockManageRequest) {
        this.mockManageRequest = mockManageRequest;
    }

    @Override
    public void onEvent(Event event) throws Throwable {
        if (event.type.equals(Event.Type.BEFORE)) {
            if (mockManageRequest.getMockType().equals(MockType.THROW_BEFORE_EXE)) {
                Class<?> throwClass = Class.forName(mockManageRequest.getReturnClassCanonicalName());
                log.info("throwsImmediately class= {}",mockManageRequest.getExceptionClassCanonicalName() );
                throwsImmediately((Throwable) throwClass.newInstance());
            }

            if (mockManageRequest.getMockType().equals(MockType.RETURN_BEFORE_EXE)) {
                Class<?> returnClass = Class.forName(mockManageRequest.getReturnClassCanonicalName());
                Object o = JSON.parseObject(mockManageRequest.getMockResponseJson(), returnClass);
                log.info("returnImmediately class= {}",mockManageRequest.getReturnClassCanonicalName() );
                returnImmediately(o);
            }
        }
    }
}
