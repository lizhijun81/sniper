package com.github.xiaoma.sniper.core.compiler.support;

import com.github.xiaoma.sniper.core.compiler.Compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by machunxiao on 2016/12/28.
 */
public abstract class AbstractCompiler implements Compiler {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z\\.]*);");
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s*\\{");

    @Override
    public Class<?> compile(String sourceCode, ClassLoader classLoader) {
        if (sourceCode == null || sourceCode.trim().length() == 0) {
            throw new IllegalArgumentException("java source code cannot be null!");
        }
        sourceCode = sourceCode.trim();
        Matcher packageMatcher = PACKAGE_PATTERN.matcher(sourceCode);
        String pkg;
        if (packageMatcher.find()) {
            pkg = packageMatcher.group(1);
        } else {
            pkg = "";
        }
        Matcher classMatcher = CLASS_PATTERN.matcher(sourceCode);
        String cls;
        if (classMatcher.find()) {
            cls = classMatcher.group(1);
        } else {
            throw new IllegalArgumentException("No such class name in code:" + sourceCode);
        }
        String className = pkg != null && !pkg.trim().equals("") ? pkg + "." + cls : cls;
        try {
            return Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            if (!sourceCode.endsWith("}")) {
                throw new IllegalStateException("The java code not endsWith \"}\", code: \n" + sourceCode + "\n");
            }
            try {
                return doCompile(className, sourceCode);
            } catch (RuntimeException re) {
                throw re;
            } catch (Throwable throwable) {
                throw new IllegalStateException("Fail to compile class,cause: " + throwable.getMessage() + " , class: " + className + " ,code:" + sourceCode, throwable);
            }
        }
    }

    protected abstract Class<?> doCompile(String className, String sourceCode) throws Throwable;

}
