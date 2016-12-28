package com.github.xiaoma.sniper.core.compiler;

import com.github.xiaoma.sniper.core.extension.SPI;

/**
 * Created by machunxiao on 2016/12/28.
 */
@SPI("jdk")
public interface Compiler {

    /**
     * compile java source code to class
     *
     * @param code
     * @param classLoader
     * @return
     */
    Class<?> compile(String code, ClassLoader classLoader);

}
