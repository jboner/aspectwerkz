package org.codehaus.aspectwerkz.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

@Target({ElementType.METHOD, ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Within {
    int modifiers() default Modifier.PRIVATE | Modifier.PROTECTED; // FIXME: etc.
    Class<? extends Annotation>[] annotations() default Null.class;

    Class type() default Null.class;
    // anonymous = Annotations only
    Class<? extends Annotation>[] value() default Null.class;
}


