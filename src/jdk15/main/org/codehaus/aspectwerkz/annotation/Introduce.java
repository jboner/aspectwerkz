package org.codehaus.aspectwerkz.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <a href="mailto:alex@gnilux.com">Alexandre Vasseur</a>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Introduce {
    String value();
}
