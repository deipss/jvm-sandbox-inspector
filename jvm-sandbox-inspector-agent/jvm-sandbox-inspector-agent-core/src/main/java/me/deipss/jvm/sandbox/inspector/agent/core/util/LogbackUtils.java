package me.deipss.jvm.sandbox.inspector.agent.core.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class LogbackUtils {

    public static void init() {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        final Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("logback.xml")) {
            configurator.doConfigure(is);
            logger.info("initializing logback success.");
        } catch (Throwable cause) {
            logger.warn("initialize logback failed.", cause);
        }
    }


}
