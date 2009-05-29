package org.codehaus.aspectwerkz.annotation;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;

@Target({ElementType.METHOD, ElementType.FIELD})
        @Retention(RetentionPolicy.RUNTIME)
        public @interface Execution {
    int modifiers() default Modifier.PRIVATE | Modifier.PROTECTED; // FIXME
    Class<? extends Annotation>[] annotations() default Null.class;

    Class returnType() default Null.class;

    String name() default "*";

    Class[] parameterTypes() default Null.class;
    // anonymous = Annotations only
    // but could be a string as well - how to do that ?
    Class<? extends Annotation>[] value() default Null.class;
}

class Null implements Annotation {
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}

