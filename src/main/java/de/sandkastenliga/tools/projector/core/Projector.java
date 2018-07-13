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


import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * A Projector is usually instatiated one time as a central component of your application.
 * In Spring Boot, for instance, you would create one instance as a Bean within your configuration to make
 * it accessible and autowirable to other components.
 * @author Guido Laures
 * @see Projection
 */
public class Projector {

    /**
     * This method returns the projection of a source object according to the rules specified on the
     * projectionClass' annotations.
     * @see Projection
     * @see ProjectionType
     * @param source The source object which should be projected onto a different target object class
     * @param projectionClass The class to be used for the projection
     * @return the projected version of source object
     */
    public <T> T project(final Object source, final Class<T> projectionClass) {
        try {
            final T result = projectionClass.newInstance();
            for (PropertyDescriptor propertyProjectedOntoDescriptor : PropertyUtils.getPropertyDescriptors(result)) {
                final Method projectMethod = PropertyUtils.getWriteMethod(propertyProjectedOntoDescriptor);
                if (projectMethod != null && (projectMethod.getAnnotation(NoProjection.class) == null)) {
                    // we can set the property and want to project
                    Projection projectionAnnotation = projectMethod.getAnnotation(Projection.class);
                    if (projectionAnnotation == null) {
                        // default is to copy the value from the property of the source object with the same name
                        PropertyUtils.setProperty(result, propertyProjectedOntoDescriptor.getName(), PropertyUtils.getProperty(source, propertyProjectedOntoDescriptor.getName()));
                    } else {
                        // determine the source object's property to map
                        // if no explicit name given, we assume the same name on the source object
                        String sourcePropertyName = (projectionAnnotation.propertyName().equals("")) ? propertyProjectedOntoDescriptor.getName() : projectionAnnotation.propertyName();
                        switch (projectionAnnotation.value()) {
                            case property:
                                // find the ID of the source class
                                Object referencedObject = PropertyUtils.getProperty(source, sourcePropertyName);
                                if (referencedObject != null) {
                                    Object referencedObjectId = PropertyUtils.getProperty(referencedObject, projectionAnnotation.referencePropertyName());
                                    PropertyUtils.setProperty(result, propertyProjectedOntoDescriptor.getName(), referencedObjectId);
                                }
                                break;
                            case projection:
                                Object sourceValueObject = PropertyUtils.getProperty(source, sourcePropertyName);
                                if (sourceValueObject != null) {
                                    Object projectionOfSourceValueObject = project(sourceValueObject, propertyProjectedOntoDescriptor.getPropertyType());
                                    PropertyUtils.setProperty(result, propertyProjectedOntoDescriptor.getName(), projectionOfSourceValueObject);
                                }
                                break;
                            case propertyCollection:
                                // get the target object colleciton and check if it is initialized
                                Collection targetCollection = (Collection)PropertyUtils.getProperty(result, propertyProjectedOntoDescriptor.getName());
                                // get the source object collection
                                Collection sourceCollection = (Collection)PropertyUtils.getProperty(source, sourcePropertyName);
                                // go through source object collection members and add reference to target object list
                                if(sourceCollection != null) {
                                    for (Object sourceCollectionEntry : sourceCollection) {
                                        Object sourceObjectId = PropertyUtils.getProperty(sourceCollectionEntry, projectionAnnotation.referencePropertyName());
                                        targetCollection.add(sourceObjectId);
                                    }
                                }
                                break;
                            case projectionCollection:
                                // get the target object colleciton and check if it is initialized
                                targetCollection = (Collection)PropertyUtils.getProperty(result, propertyProjectedOntoDescriptor.getName());
                                // get the source object collection
                                sourceCollection = (Collection)PropertyUtils.getProperty(source, sourcePropertyName);
                                // go through source object collection members and add reference to target object list
                                if(sourceCollection != null) {
                                    final Type[] types = projectMethod.getGenericParameterTypes();
                                    // assuming that the first parameter to the method is of type List<Integer>
                                    final ParameterizedType pType = (ParameterizedType) types[0];
                                    final Class<?> clazz = (Class<?>) pType.getActualTypeArguments()[0];
                                    for (Object sourceCollectionEntry : sourceCollection) {
                                        targetCollection.add(project(sourceCollectionEntry, clazz));
                                    }
                                }
                                break;
                            default:
                                PropertyUtils.setProperty(result, propertyProjectedOntoDescriptor.getName(), PropertyUtils.getProperty(source, sourcePropertyName));
                        }
                    }
                }
            }
            return result;
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        }
    }

}
