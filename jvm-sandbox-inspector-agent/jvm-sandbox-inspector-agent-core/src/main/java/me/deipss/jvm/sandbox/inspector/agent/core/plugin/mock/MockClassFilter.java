package me.deipss.jvm.sandbox.inspector.agent.core.plugin.mock;

import com.alibaba.jvm.sandbox.api.filter.Filter;

public class MockClassFilter implements Filter {
    private final String className;

    public MockClassFilter(String className) {
        this.className = className;
    }

    @Override
    public boolean doClassFilter(int access, String javaClassName, String superClassTypeJavaClassName, String[] interfaceTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {
        return className.equals(javaClassName);
    }

    @Override
    public boolean doMethodFilter(int access, String javaMethodName, String[] parameterTypeJavaClassNameArray, String[] throwsTypeJavaClassNameArray, String[] annotationTypeJavaClassNameArray) {
        return true;
    }
}
