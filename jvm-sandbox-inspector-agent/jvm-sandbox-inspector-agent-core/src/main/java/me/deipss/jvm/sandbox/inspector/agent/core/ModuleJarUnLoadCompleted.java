package me.deipss.jvm.sandbox.inspector.agent.core;

import com.alibaba.jvm.sandbox.api.spi.ModuleJarUnLoadSpi;
import me.deipss.jvm.sandbox.inspector.agent.core.util.LogbackUtils;
import org.kohsuke.MetaInfServices;

/**
 * {@link }
 * <p>
 *
 */
@MetaInfServices(ModuleJarUnLoadSpi.class)
public class ModuleJarUnLoadCompleted implements ModuleJarUnLoadSpi {

    @Override
    public void onJarUnLoadCompleted() {
        try {
            LogbackUtils.destroy();
        } catch (Throwable e) {
            // ignore
        }
    }
}
