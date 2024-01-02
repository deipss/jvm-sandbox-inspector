package me.deipss.jvm.sandbox.inspector.agent.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageRequest;
import me.deipss.jvm.sandbox.inspector.agent.api.service.HeartBeatService;
import me.deipss.jvm.sandbox.inspector.agent.api.service.InvocationSendService;
import me.deipss.jvm.sandbox.inspector.agent.api.service.MockManageService;
import me.deipss.jvm.sandbox.inspector.agent.core.impl.HeartBeatServiceImpl;
import me.deipss.jvm.sandbox.inspector.agent.core.impl.InvocationSendServiceImpl;
import me.deipss.jvm.sandbox.inspector.agent.core.impl.MockManageServiceImpl;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.concurrent.TtlConcurrentPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo.DubboConsumerPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.dubbo.DubboProviderPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.http.HttpPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc.JdbcPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket.RocketMqConsumerPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.rocket.RocketMqSendPlugin;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@MetaInfServices(Module.class)
@Information(id = Constant.moduleId, author = "deipss666@gmail.com", version = Constant.version)
@Slf4j
public class InspectorModule implements Module, ModuleLifecycle {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Resource
    private ConfigInfo configInfo;

    @Resource
    private ModuleManager moduleManager;

    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    private MockManageService mockManageService;
    private HeartBeatService heartBeatService;
    private InvocationSendService invocationSend;


    @Override
    public void onLoad() throws Throwable {

    }

    @Override
    public void onUnload() throws Throwable {

    }

    @Override
    public void onActive() throws Throwable {

    }

    @Override
    public void onFrozen() throws Throwable {
    }

    @Override
    public void loadCompleted() {
        invocationSend = new InvocationSendServiceImpl();

        JdbcPlugin jdbcPlugin = new JdbcPlugin(invocationSend);
        jdbcPlugin.watch(moduleEventWatcher);

        TtlConcurrentPlugin ttlConcurrentPlugin = new TtlConcurrentPlugin();
        ttlConcurrentPlugin.watch(moduleEventWatcher);

        DubboConsumerPlugin dubboConsumerPlugin = new DubboConsumerPlugin(invocationSend);
        dubboConsumerPlugin.watch(moduleEventWatcher);

        DubboProviderPlugin dubboProviderPlugin = new DubboProviderPlugin(invocationSend);
        dubboProviderPlugin.watch(moduleEventWatcher);

        HttpPlugin httpPlugin = new HttpPlugin(invocationSend);
        httpPlugin.watch(moduleEventWatcher);

        RocketMqConsumerPlugin rocketMqConsumerPlugin = new RocketMqConsumerPlugin(invocationSend);
        rocketMqConsumerPlugin.watch(moduleEventWatcher);

        RocketMqSendPlugin rocketMqSendPlugin = new RocketMqSendPlugin(invocationSend);
        rocketMqSendPlugin.watch(moduleEventWatcher);

        mockManageService = new MockManageServiceImpl(moduleEventWatcher);
        heartBeatService = new HeartBeatServiceImpl();
        heartBeatService.start();
    }

    @Command("manageMock")
    public void manageMock(HttpServletRequest request, HttpServletResponse response){
        log.info(request.getPathInfo());
        MockManageRequest mock = JSON.parseObject(request.getParameter("mock"), MockManageRequest.class);
        List<MockManageRequest> mockList = JSON.parseArray(request.getParameter("mockList"), MockManageRequest.class);
        switch (mock.getOperate()) {
            case ADD:mockManageService.add(mock);
                break;
            case VIEW:mockManageService.view();
                break;
            case STOP:mockManageService.stop(mock.getMockId());
                break;
            case ADD_ALL:mockManageService.addAll(mockList);
                break;
        }
    }


}
