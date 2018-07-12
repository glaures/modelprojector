package de.sandkastenliga.tools.projector.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Projection {

    ProjectionType value() default ProjectionType.property;

    String propertyName() default "";

    String idPropertyName() default "";

}
