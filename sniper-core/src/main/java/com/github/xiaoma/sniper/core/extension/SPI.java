package com.github.xiaoma.sniper.core.extension;

import java.lang.annotation.*;

/**
 * Created by machunxiao on 16/12/26.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SPI {

    /**
     * 缺省扩展点名。
     */
    String value() default "";

}