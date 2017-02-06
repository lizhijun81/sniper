package com.github.xiaoma.sniper.core;

import com.github.xiaoma.sniper.core.compiler.Compiler;
import com.github.xiaoma.sniper.core.extension.ExtensionLoader;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by machunxiao on 17/2/6.
 */
public class CompilerTest {

    private static final Logger logger = LoggerFactory.getLogger(CompilerTest.class);

    @Test
    public void testCompiler() {

        Compiler compiler = ExtensionLoader.getExtensionLoader(Compiler.class).getAdaptiveExtension();

        String code = "package a.b.c;public class Abc{private int age;}";
        ClassLoader loader = getClass().getClassLoader();
        Class<?> cls = compiler.compile(code, loader);
        System.out.println(cls);
        logger.info("class:{}.", cls.getName());

    }
}
