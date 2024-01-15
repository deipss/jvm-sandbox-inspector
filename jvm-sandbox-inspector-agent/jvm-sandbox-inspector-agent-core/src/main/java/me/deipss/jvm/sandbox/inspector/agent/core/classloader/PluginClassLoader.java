package me.deipss.jvm.sandbox.inspector.agent.core.classloader;

import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class PluginClassLoader extends URLClassLoader {

    private final List<String> classRegexList = new ArrayList<>(8);
    private final LoadedClassDataSource loadedClassDataSource;

    public PluginClassLoader(URL[] urls, ClassLoader parent, List<String> classRegexList, LoadedClassDataSource loadedClassDataSource) {
        super(urls, parent);
        if (null != classRegexList && !classRegexList.isEmpty()) {
            this.classRegexList.addAll(classRegexList);
        }
        this.loadedClassDataSource = loadedClassDataSource;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // plugin-api的类;slf4j;logback；使用模块加载
        if (userParent(name)) {
            try {
                return super.loadClass(name, resolve);
            } catch (Exception e) {
                // ignore
            }
        }
        // 使用业务类加载器
        if (null != classRegexList && !classRegexList.isEmpty()) {
            for (final String classRegex : classRegexList) {
                if (!name.matches(classRegex)) {
                    continue;
                }
                int cnt = 10;
                try {
                    Iterator<Class<?>> iterator = loadedClassDataSource.iteratorForLoadedClasses();
                    do {
                        while (iterator.hasNext()) {
                            final Class<?> next = iterator.next();
                            if (name.equals(next.getName()) && !isSandboxLoadedClass(next)) {
                                Class<?> aClass = next.getClassLoader().loadClass(name);
                                log.info("{} load {} done", next.getClassLoader().getClass().getCanonicalName(), name);
                                return aClass;
                            }
                        }
                        Thread.sleep(100);
                    } while (--cnt > 0);
                } catch (Exception e) {
                    log.error(" load {} error,cnt={}", name, cnt, e);
                }
            }
        }

        // 先查一次已加载类的缓存
        final Class<?> loadedClass = findLoadedClass(name);

        if (loadedClass != null) {
            return loadedClass;
        }

        try {
            final Class<?> aClass = findClass(name);
            if (resolve) {
                resolveClass(aClass);
            }
            return aClass;
        } catch (Exception e) {
            return super.loadClass(name, resolve);
        }
    }

    /**
     * 是否sandbox classloader加载的类
     *
     * @param clazz 目标类
     * @return true / false
     */
    private boolean isSandboxLoadedClass(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (clazz.getClassLoader() == null) {
            return false;
        }
        return clazz.getClassLoader().getClass().getName().contains("sandbox");
    }

    /**
     * 是否使用父类加载（理论上PluginClassLoader除了特殊路由表之外的都可以用moduleClassloader的类）
     * <p>
     * 但为了插件能够更自由的引包，也破坏了双亲委派机制
     *
     * @param name 类名
     * @return 是否使用父类加载
     */
    private boolean userParent(String name) {
        for (String pattern : Constant.PLUGIN_CLASS_PATTERN) {
            if (name.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
