package me.deipss.jvm.sandbox.inspector.agent.api;

public class Constant {

    /**
     * 版本号
     */
    public static final String version = "1.0.11";

    /**
     * 模块的ID
     */
    public static final String moduleId = "inspector";

    /**
     * 常量
     */
    public static final String DUBBO_CONSUMER = "DUBBO_CONSUMER";
    public static final String DUBBO_PROVIDER = "DUBBO_PROVIDER";
    public static final String ROCKET_MQ_SEND = "ROCKET_MQ_SEND";
    public static final String ROCKET_MQ_CONSUMER = "ROCKET_MQ_CONSUMER";
    public static final String JDBC = "JDBC";
    public static final String HTTP = "HTTP";

    /**
     * 使用jvm-sandbox加载器去加载
     * 不使用jvm-sandbox-inspector的类加载去加载
     */
    public static final String[] PLUGIN_CLASS_PATTERN_FOR_PARENT = new String[]{
            "^org.slf4j..*",
            "^ch.qos.logback..*",
            "^org.apache.commons..*"
    };


    /**
     * 使用业务类加载器去加载
     */
    public static final String[] PLUGIN_CLASS_PATTERN_FOR_BIZ = new String[]{
            "^org.apache.dubbo..*","^javax.servlet..*"
    };

}
