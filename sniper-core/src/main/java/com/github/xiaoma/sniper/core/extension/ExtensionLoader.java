package com.github.xiaoma.sniper.core.extension;

import com.github.xiaoma.sniper.core.compiler.Compiler;
import com.github.xiaoma.sniper.core.utils.Holder;
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
    private static final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();
    private static final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private final Class<?> type;
    private final ExtensionFactory factory;

    private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
    private volatile Throwable createAdaptiveInstanceError = null;
    private volatile String cachedDefaultName;
    private volatile Class<?> cachedAdaptiveClass;

    private ExtensionLoader(Class<?> type) {
        this.type = type;
        this.factory = type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension();
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
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Extension name == null");
        }
        if ("true".equals(name)) {
            return getDefaultExtension();
        }
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }

    public T getAdaptiveExtension() {
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            if (createAdaptiveInstanceError != null) {
                throw new IllegalStateException("fail to create adaptive instance: " + createAdaptiveInstanceError.getMessage(), createAdaptiveInstanceError);
            }
            synchronized (cachedAdaptiveInstance) {
                instance = cachedAdaptiveInstance.get();
                if (instance == null) {
                    try {
                        instance = createAdaptiveExtension();
                        cachedAdaptiveInstance.set(instance);
                    } catch (Throwable e) {
                        createAdaptiveInstanceError = e;
                        throw new IllegalStateException("fail to create adaptive instance: " + e.getMessage(), e);
                    }
                }
            }
        }
        return (T) instance;
    }

    public T getDefaultExtension() {
        getExtensionClasses();
        if (cachedDefaultName == null || cachedDefaultName.length() == 0 || "true".equals(cachedDefaultName)) {
            return null;
        }
        return getExtension(cachedDefaultName);
    }


    private Class<?> getAdaptiveExtensionClass() {
        getExtensionClasses();
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }

    private T createAdaptiveExtension() {
        try {
            T instance = (T) getAdaptiveExtensionClass().newInstance();
            return injectExtension(instance);
        } catch (Exception e) {
            logger.error("", e);
            throw new IllegalStateException("");
        }
    }

    private Class<?> createAdaptiveExtensionClass() {
        String code = createAdaptiveExtensionClassCode();
        ClassLoader classLoader = findClassLoader();
        Compiler compiler = ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();
        return compiler.compile(code, classLoader);
    }

    private String createAdaptiveExtensionClassCode() {
        return null;
    }

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
                Object obj = factory.getExtension(parameterTypes[0], property);
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
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    private Map<String, Class<?>> loadExtensionClasses() {
        SPI spi = type.getAnnotation(SPI.class);
        if (spi != null) {
            String defaultSPIName = spi.value().trim();
            if (defaultSPIName.length() > 0) {
                cachedDefaultName = defaultSPIName;
            }
        }
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        loadFile(extensionClasses, ""); // resources目录下
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
                            logger.debug("This line:`{}` is comment", line);
                            continue;
                        }
                        int i = line.indexOf('=');
                        String name = line.substring(0, i).trim();
                        String clsName = line.substring(i + 1).trim();
                        Class<?> cls = Class.forName(clsName, true, classLoader);
                        if (!type.isAssignableFrom(cls)) {
                            throw new IllegalStateException(cls.getName() + " is not subtype of interface:" + type);
                        }
                        if (cls.isAnnotationPresent(Adaptive.class)) {
                            if (cachedAdaptiveClass == null) {
                                cachedAdaptiveClass = cls;
                            }
                        }
                        extensionClasses.put(name, cls);
                        logger.info("{}:{} initialized.", name, clsName);
                    }
                } catch (IOException ex) {
                    logger.error("", ex);
                }
            }
        } catch (Throwable e) {
            logger.error("", e);
        }

    }

    private static ClassLoader findClassLoader() {
        return ExtensionLoader.class.getClassLoader();
    }

}
