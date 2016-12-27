package com.github.xiaoma.sniper.core.extension;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 16/12/26.
 */
@SuppressWarnings("all")
public class ExtensionLoader<T> {

    private static final Logger logger = LoggerFactory.getLogger(ExtensionLoader.class);

    private static final Map<Class<?>, ExtensionLoader<?>> extensionLoaders = new ConcurrentHashMap<>();
    private static final Map<Class<?>, String> cachedNames = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> cachedClasses = new ConcurrentHashMap<>();
    private static final Map<String, Object> cachedInstances = new ConcurrentHashMap<>();

    private final Class<?> type;
    private final ExtensionFactory factory;

    private String cachedName;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
        this.factory = type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getExtension("");
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);
        if (loader == null) {
            extensionLoaders.putIfAbsent(type, new ExtensionLoader<>(type));
            loader = (ExtensionLoader<T>) extensionLoaders.get(type);
        }
        return loader;
    }

    public T getExtension(String name) {
        T extension = (T) cachedInstances.get(name);
        if (extension == null) {
            cachedInstances.putIfAbsent(name, createExtension(name));
            extension = (T) cachedInstances.get(name);
        }
        return extension;
    }

//    public T getAdaptiveExtension() {
//        return createAdaptiveExtension();
//    }
//
//    private T createAdaptiveExtension() {
//        getExtensionClasses();
//
//        try {
//            T instance = (T) getAdaptiveExtensionClass().newInstance();
//            return injectExtension(instance);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalStateException("");
//        }
//    }
//
//    private Class<?> getAdaptiveExtensionClass() {
//
//        return null;
//    }

    private T createExtension(String name) {
        Class<?> cls = getExtensionClasses().get(name);
        if (cls == null) {
            throw new IllegalStateException("No such extension " + type.getName() + " by name " + name);
        }
        try {
            T instance = (T) cls.newInstance();
            injectExtension(instance);
            return instance;
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
    }

    private T injectExtension(T instance) {
        Method[] methods = instance.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set")
                    && method.getParameterTypes().length == 1
                    && Modifier.isPublic(method.getModifiers())) { // public void setAbc(Object obj){}
                Class<?>[] parameterTypes = method.getParameterTypes();
                String property = methodName.length() > 3 ? methodName.substring(3, 4).toLowerCase() + methodName.substring(4) : "";
                Object obj = factory.getExtension(parameterTypes[0], "");
                if (obj != null) {
                    try {
                        method.invoke(instance, obj);
                    } catch (Exception e) {
                        logger.error("", e);
                    }
                }
            }
        }
        return instance;
    }

    private Map<String, Class<?>> getExtensionClasses() {
        if (cachedClasses.isEmpty()) {
            Map<String, Class<?>> classes = loadExtensionClasses();
            cachedClasses.putAll(classes);
        }
        return cachedClasses;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            String defaultSPIName = spi.value().trim();
            if (defaultSPIName.length() > 0) {
                cachedName = defaultSPIName;
            }
        }
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        loadFile(extensionClasses, "");
        loadFile(extensionClasses, "META-INF/services/");
        loadFile(extensionClasses, "META-INF/sniper/");
        return extensionClasses;
    }

    private void loadFile(Map<String, Class<?>> extensionClasses, String dir) {
        String fileName = dir == null ? "" : dir + type.getName();
        try {
            Enumeration<URL> urls;
            ClassLoader classLoader = findClassLoader();
            if (classLoader != null) {
                urls = classLoader.getResources(fileName);
            } else {
                urls = ClassLoader.getSystemResources(fileName);
            }
            System.out.println(urls);
            if (urls == null) {
                return;
            }
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    while ((line = reader.readLine()) != null && (line = line.trim()).length() > 0) {
                        if (line.indexOf('#') == 0) {
                            logger.debug("This line:`{}` is note", line);
                            continue;
                        }
                        int i = line.indexOf('=');
                        String name = line.substring(0, i).trim();
                        String clsName = line.substring(i + 1).trim();
                        Class<?> cls = Class.forName(clsName, true, classLoader);
                        extensionClasses.put(name, cls);
                        logger.info("{}:{} initialized.", name, clsName);
                    }
                } catch (IOException ex) {

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private static ClassLoader findClassLoader() {
        return ExtensionLoader.class.getClassLoader();
    }

}
