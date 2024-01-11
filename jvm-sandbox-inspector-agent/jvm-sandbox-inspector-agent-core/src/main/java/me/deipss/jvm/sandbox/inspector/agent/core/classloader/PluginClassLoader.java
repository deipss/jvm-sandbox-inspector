package me.deipss.jvm.sandbox.inspector.agent.core.classloader;

import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.CollectionUtils;

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
        if (CollectionUtils.isNotEmpty(classRegexList)) {
            this.classRegexList.addAll(classRegexList);
        }
        this.loadedClassDataSource = loadedClassDataSource;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        // 使用业务类加载器
        if (CollectionUtils.isNotEmpty(classRegexList)) {
            for (final String classRegex : classRegexList) {
                if (!name.matches(classRegex)) {
                    continue;
                }
                try {
                    int cnt = 10;
                    Iterator<Class<?>> iterator = loadedClassDataSource.iteratorForLoadedClasses();
                    do {
                        while (iterator.hasNext()) {
                            final Class<?> next = iterator.next();
                            if (name.equals(next.getName()) && !isSandboxLoadedClass(next)) {
                                return next.getClassLoader().loadClass(name);
                            }
                        }
                        Thread.sleep(100);
                    } while (--cnt > 0);
                } catch (Exception e) {
                    log.error(" load {} error", name, e);
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
}
