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
import me.deipss.jvm.sandbox.inspector.agent.api.domain.MockManageResponse;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

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
    public void manageMock(HttpServletRequest request, HttpServletResponse response) {
        log.info(request.getPathInfo());
        String body = null;
        try (BufferedReader reader = request.getReader()) {
            body = reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MockManageRequest mock = JSON.parseObject(body, MockManageRequest.class);
        switch (mock.getOperate()) {
            case VIEW:
                MockManageResponse view = mockManageService.view();
                responseResult(response, view);
                break;
            case STOP:
                int rst = 0;
                for (MockManageRequest.Inner inner : mock.getMockInnerList()) {
                    rst += mockManageService.stop(inner.getMockId());
                }
                responseResult(response, rst);
                break;
            case ADD_ALL:
                int i = mockManageService.addAll(mock.getMockInnerList());
                responseResult(response, i);
                break;
        }
    }

    private void responseResult(HttpServletResponse response, Object result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/json");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
//        ServletOutputStream out = null;
        try (PrintWriter writer = response.getWriter()) {
            writer.write(JSON.toJSONString(result));
            writer.flush();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
