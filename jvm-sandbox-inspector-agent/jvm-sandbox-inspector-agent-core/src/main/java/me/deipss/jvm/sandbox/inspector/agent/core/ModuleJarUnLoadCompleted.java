package me.deipss.jvm.sandbox.inspector.agent.core;

import com.alibaba.jvm.sandbox.api.spi.ModuleJarUnLoadSpi;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.MetaInfServices;


@Slf4j
@MetaInfServices(ModuleJarUnLoadSpi.class)
public class ModuleJarUnLoadCompleted implements ModuleJarUnLoadSpi {
    @Override
    public void onJarUnLoadCompleted() {
        log.info("onJarUnLoadCompleted success");
    }
}
