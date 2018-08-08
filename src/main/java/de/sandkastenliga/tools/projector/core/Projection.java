/*
 * (C) Copyright 2018 Sandkastenliga (http://www.sandkastenliga.de).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.sandkastenliga.tools.projector.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated setter method of a projected property of the target object (projection)
 * will be used to project data from the source object (object thet is projected)
 * according to the rules specified in this annotation.
 * @author Guido Laures
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Projection {

    /**
     * The type of projection to be used by the Projector.
     * @see ProjectionType
     * @return the type of projection to be used
     */
    ProjectionType value() default ProjectionType.asIs;

    /**
     * The name of the property on the source object to be projected onto this
     * setter method. In case the name is the same for source and target no value needs to be
     * provided.
     * Note: the property types need to be the same or at least wrappable into each other (e.g. Long with long will work out of the box)
     * @return the name of the property on the source object to be projected
     */
    String propertyName() default "";

    /**
     * This property is only required if you use ProjectionType.property or ProjectionType.propertyCollection.
     * It tells the Projector the name of the property on the source object's property value to be used as value.
     * Note: you need to make sure that the property of the target object has the same or wrappable type as the
     * the source object's property's property.
     * Default value for this annotation parameter is "id".
     * @return the name of the property of the source object's property to be used as value for the annotated setter method
     */
    String referencePropertyName() default "id";
}
