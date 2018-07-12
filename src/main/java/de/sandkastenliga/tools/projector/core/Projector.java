package de.sandkastenliga.tools.projector.core;


import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class Projector {

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
                            case reference:
                                // find the ID of the source class
                                Object referencedObject = PropertyUtils.getProperty(source, sourcePropertyName);
                                if (referencedObject != null) {
                                    String referencedObjectIdPropertyName = ("".equals(projectionAnnotation.idPropertyName()) ? "id" : projectionAnnotation.idPropertyName());
                                    Object referencedObjectId = PropertyUtils.getProperty(referencedObject, referencedObjectIdPropertyName);
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
                            case referenceList:

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
