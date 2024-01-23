package me.deipss.jvm.sandbox.inspector.agent.core.classloader;

import com.alibaba.jvm.sandbox.api.resource.LoadedClassDataSource;
import lombok.extern.slf4j.Slf4j;
import me.deipss.jvm.sandbox.inspector.agent.api.Constant;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PluginClassLoader extends URLClassLoader {

    private final LoadedClassDataSource loadedClassDataSource;

    public PluginClassLoader(URL[] urls, ClassLoader parent, LoadedClassDataSource loadedClassDataSource) {
        super(urls, parent);
        this.loadedClassDataSource = loadedClassDataSource;
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // slf4j;logback；使用模块加载
        if (userParent(name)) {
            try {
                log.info(" inspector load {} by parent", name);
                return super.loadClass(name, resolve);
            } catch (Exception e) {
                log.error(" inspector super class ={} load error,", super.getClass().getCanonicalName(), e);
            }
        }
        // 使用业务类加载器
        for (final String classRegex : Constant.PLUGIN_CLASS_PATTERN_FOR_BIZ) {
            if (!name.matches(classRegex)) {
                continue;
            }
            log.info(" inspector load {} by biz", name);
            ArrayList<ClassLoader> classLoaders = new ArrayList<>(2);
            int cnt = 10;
            try {
                Iterator<Class<?>> iterator = loadedClassDataSource.iteratorForLoadedClasses();
                do {
                    while (iterator.hasNext()) {
                        final Class<?> next = iterator.next();
                        if (name.equals(next.getName()) && !isSandboxLoadedClass(next)) {
                            classLoaders.add(next.getClassLoader());
                        }
                    }
                    Thread.sleep(100);
                } while (--cnt > 0);
                if (classLoaders.size()==1) {
                    Class<?> aClass = classLoaders.get(0).loadClass(name);
                    log.info(" inspector 1 class loader, {} load {} done", classLoaders.get(0).getClass().getCanonicalName(), name);
                    return aClass;
                }
                if (classLoaders.size()>1) {
                    log.info(" inspector {} class loader, {} load {} ",classLoaders.size(), classLoaders.stream().map(i->i.getClass().getCanonicalName()).toArray(), name);
                    if("javax.servlet.ServletOutputStream".equals(name)){
                        Optional<ClassLoader> sandbox = classLoaders.stream().filter(i -> i.getClass().getCanonicalName().contains("sandbox")).findAny();
                        if(sandbox.isPresent()){
                            return sandbox.get().loadClass(name);
                        }
                    }else{
                        Optional<ClassLoader> biz = classLoaders.stream().filter(i -> !i.getClass().getCanonicalName().contains("sandbox")).findAny();
                        if(biz.isPresent()){
                            return biz.get().loadClass(name);
                        }
                    }
                }
            } catch (Exception e) {
                log.error(" inspector  load {} error,cnt={}", name, cnt, e);
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
            log.info("finally use super class loader ,name = {},super loader={},error={}",name ,super.getClass().getCanonicalName(),e.getMessage());
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
     * @param name 类名
     * @return 是否使用父类加载
     */
    private boolean userParent(String name) {
        for (String pattern : Constant.PLUGIN_CLASS_PATTERN_FOR_PARENT) {
            if (name.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
