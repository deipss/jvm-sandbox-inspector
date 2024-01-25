package me.deipss.jvm.sandbox.inspector.agent.api;

public class Constant {

    public static final String version = "1.0.11";
    public static final String moduleId = "inspector";
    public static final String DUBBO_CONSUMER = "DUBBO_CONSUMER";
    public static final String DUBBO_PROVIDER = "DUBBO_PROVIDER";
    public static final String ROCKET_MQ_SEND = "ROCKET_MQ_SEND";
    public static final String ROCKET_MQ_CONSUMER = "ROCKET_MQ_CONSUMER";
    public static final String JDBC = "JDBC";
    public static final String HTTP = "HTTP";

    public static final String[] PLUGIN_CLASS_PATTERN_FOR_PARENT = new String[]{
            "^org.slf4j..*",
            "^ch.qos.logback..*",
            "^org.apache.commons..*"
    };


    public static final String[] PLUGIN_CLASS_PATTERN_FOR_BIZ = new String[]{
            "^org.apache.dubbo..*","^javax.servlet..*"
    };

}
