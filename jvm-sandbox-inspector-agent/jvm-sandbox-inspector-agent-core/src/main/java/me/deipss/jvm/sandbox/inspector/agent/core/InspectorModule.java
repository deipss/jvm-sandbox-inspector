package me.deipss.jvm.sandbox.inspector.agent.core;

import com.alibaba.jvm.sandbox.api.Information;
import com.alibaba.jvm.sandbox.api.Module;
import com.alibaba.jvm.sandbox.api.ModuleLifecycle;
import com.alibaba.jvm.sandbox.api.annotation.Command;
import com.alibaba.jvm.sandbox.api.resource.ConfigInfo;
import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import com.alibaba.jvm.sandbox.api.resource.ModuleEventWatcher;
import com.alibaba.jvm.sandbox.api.resource.ModuleManager;
import com.sun.media.jfxmedia.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.concurrent.TtlConcurrentAdvice;
import me.deipss.jvm.sandbox.inspector.agent.core.plugin.jdbc.JdbcPlugin;
import me.deipss.jvm.sandbox.inspector.agent.core.util.InvocationSendUtil;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.RequestWrapper;

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
        JdbcPlugin jdbcPlugin = new JdbcPlugin();
        jdbcPlugin.watch(moduleEventWatcher);
        TtlConcurrentAdvice ttlConcurrentAdvice = new TtlConcurrentAdvice();
        ttlConcurrentAdvice.watch(moduleEventWatcher);
        InvocationSendUtil.start();
    }

    @Command("manageMock")
    public void manageMock(HttpServletRequest request, HttpServletResponse response){
        log.info(request.getPathInfo());

    }


}
