package xiaoma.github.appweb.web.annotatioin;

import java.lang.annotation.*;

/**
 * Created by Dell on 2015/5/5.
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface Servlet {
    String value() default "";
}
