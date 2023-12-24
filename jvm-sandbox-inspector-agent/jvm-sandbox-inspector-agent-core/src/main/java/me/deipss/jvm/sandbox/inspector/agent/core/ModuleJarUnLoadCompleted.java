package me.deipss.jvm.sandbox.inspector.agent.core;

import com.alibaba.jvm.sandbox.api.spi.ModuleJarUnLoadSpi;
import org.kohsuke.MetaInfServices;


@MetaInfServices(ModuleJarUnLoadSpi.class)

public class ModuleJarUnLoadCompleted implements ModuleJarUnLoadSpi {

    @Override
    public void onJarUnLoadCompleted() {

    }
}
