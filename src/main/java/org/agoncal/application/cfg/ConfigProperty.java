package org.agoncal.application.cfg;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.*;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER,
        ElementType.TYPE})
@Documented
public @interface ConfigProperty {
    @Nonbinding String value() default "";
}
