package com.github.xiaoma.sniper.core.compiler.support;

/**
 * Created by machunxiao on 2016/12/28.
 */
public class JdkCompiler extends AbstractCompiler {

    @Override
    protected Class<?> doCompile(String className, String sourceCode) throws Throwable {
        return CompilerUtils.compile(className, sourceCode);
    }

}
