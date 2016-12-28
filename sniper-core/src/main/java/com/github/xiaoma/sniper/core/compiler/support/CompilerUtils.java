package com.github.xiaoma.sniper.core.compiler.support;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by machunxiao on 2016/12/28.
 */
public enum CompilerUtils {
    ;

    private static final JavaCompiler compiler;
    private static final StandardJavaFileManager manager;
    private static final MyFileManager myManager;
    private static final Method defineClass;

    private static final Map<ClassLoader, ConcurrentHashMap<String, Class<?>>> loadedClasses = new ConcurrentHashMap<>();
    private static final Map<String, Class<?>> cachedCompiledClasses = new ConcurrentHashMap<>();


    static {
        compiler = ToolProvider.getSystemJavaCompiler();
        manager = compiler.getStandardFileManager(null, null, null);
        myManager = new MyFileManager(manager);
        Method tempDefineClass;
        try {
            tempDefineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            tempDefineClass.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            tempDefineClass = null;
        }
        defineClass = tempDefineClass;
    }

    public static ClassLoader getCallerClassLoader(Class<?> cls) {
        ClassLoader classLoader;
        try {
            classLoader = CompilerUtils.class.getClassLoader();
        } catch (Exception ex) {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        if (classLoader == null && cls != null) {
            classLoader = cls.getClassLoader();
        }
        return classLoader;
    }

    public static Class<?> compile(String className, String javaCode) throws ClassNotFoundException {

        Class<?> retCls = cachedCompiledClasses.get(className);
        if (retCls != null) {
            return retCls;
        }
        JavaSourceFromString javaSourceFromString = new JavaSourceFromString(className, javaCode);
        Iterable<? extends JavaFileObject> compilationUnits = Collections.singleton(javaSourceFromString);
        Boolean result = compiler.getTask(null, myManager, null, null, null, compilationUnits).call();
        if (result == null || !result) {
            throw new RuntimeException("Compiler Failed, ClassName: [" + className + "],JavaCode: [" + javaCode + "].");
        }
        Map<String, byte[]> buffers = myManager.getAllBuffers();
        for (Map.Entry<String, byte[]> entry : buffers.entrySet()) {
            String clsName = entry.getKey();
            byte[] bytes = entry.getValue();
            if (clsName.equals(className)) {
                ClassLoader classLoader = CompilerUtils.class.getClassLoader();
                if (classLoader == null) {
                    classLoader = Thread.currentThread().getContextClassLoader();
                }
                retCls = defineClass(classLoader, className, bytes);
                cachedCompiledClasses.putIfAbsent(javaCode, retCls);
                return cachedCompiledClasses.get(javaCode);
            }
        }
        return null;
    }

    public static Class<?> defineClass(ClassLoader classLoader, String className, byte[] data) {
        try {
            return (Class<?>) defineClass.invoke(classLoader, className, data, 0, data.length);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {

        private final String code;

        protected JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }


        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return code;
        }
    }

    private static class MyFileManager implements JavaFileManager {

        private final Map<String, ByteArrayOutputStream> buffers = new ConcurrentHashMap<>();
        private final StandardJavaFileManager standardJavaFileManager;

        public MyFileManager(StandardJavaFileManager standardJavaFileManager) {
            this.standardJavaFileManager = standardJavaFileManager;
        }

        @Override
        public ClassLoader getClassLoader(Location location) {
            return standardJavaFileManager.getClassLoader(location);
        }

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds, boolean recurse) throws IOException {
            return standardJavaFileManager.list(location, packageName, kinds, recurse);
        }

        @Override
        public String inferBinaryName(Location location, JavaFileObject file) {
            return standardJavaFileManager.inferBinaryName(location, file);
        }

        @Override
        public boolean isSameFile(FileObject a, FileObject b) {
            return standardJavaFileManager.isSameFile(a, b);
        }

        @Override
        public boolean handleOption(String current, Iterator<String> remaining) {
            return standardJavaFileManager.handleOption(current, remaining);
        }

        @Override
        public boolean hasLocation(Location location) {
            return standardJavaFileManager.hasLocation(location);
        }

        @Override
        public JavaFileObject getJavaFileForInput(Location location, String className, JavaFileObject.Kind kind) throws IOException {
            if (location == StandardLocation.CLASS_OUTPUT && kind == JavaFileObject.Kind.CLASS && buffers.containsKey(className)) {
                byte[] data = buffers.get(className).toByteArray();
                return new SimpleJavaFileObject(URI.create(className), kind) {
                    @Override
                    public InputStream openInputStream() throws IOException {
                        return new ByteArrayInputStream(data);
                    }
                };
            }
            return standardJavaFileManager.getJavaFileForInput(location, className, kind);
        }

        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            return new SimpleJavaFileObject(URI.create(className), kind) {
                @Override
                public OutputStream openOutputStream() throws IOException {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    buffers.put(className, byteArrayOutputStream);
                    return byteArrayOutputStream;
                }
            };
        }

        @Override
        public FileObject getFileForInput(Location location, String packageName, String relativeName) throws IOException {
            return standardJavaFileManager.getFileForInput(location, packageName, relativeName);
        }

        @Override
        public FileObject getFileForOutput(Location location, String packageName, String relativeName, FileObject sibling) throws IOException {
            return standardJavaFileManager.getFileForOutput(location, packageName, relativeName, sibling);
        }

        @Override
        public void flush() throws IOException {
            standardJavaFileManager.flush();
        }

        @Override
        public void close() throws IOException {
            standardJavaFileManager.close();
        }

        @Override
        public int isSupportedOption(String option) {
            return standardJavaFileManager.isSupportedOption(option);
        }

        public void clear() {
            buffers.clear();
        }

        private Map<String, byte[]> getAllBuffers() {
            Map<String, byte[]> result = new ConcurrentHashMap<>(buffers.size() * 2);
            for (Map.Entry<String, ByteArrayOutputStream> entry : buffers.entrySet()) {
                result.put(entry.getKey(), entry.getValue().toByteArray());
            }
            return result;
        }
    }
}
