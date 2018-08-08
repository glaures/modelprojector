package de.sandkastenliga.tools.projector.core;

/**
 * Tells the {@link Projector} which strategy to use for the projection of the annotated property.
 * @see Projector
 * @see Projection#value()
 * @author Guido Laures
 */
public enum ProjectionType {

    /**
     * Ignore this property in tthe projection even if there is a property on the source object with the same name.
     */
    none,
    /**
     * Project this property as is from the source object onto the target object.
     * This is the default behavior and can be omitted if source and target property share the
     * same name.
     */
    asIs,
    /**
     * The projection of this property will not use the source object's property value but a property
     * of it. This is useful if you do not want full copies of non-simple property types leading to huge
     * object hierarchies but rather project a property as reference only which can be used for further
     * processing as needed.
     * You will have to specify the property name of the source target's property object to let Projector
     * know what property to use as reference.
     * @see Projection#referencePropertyName()
     */
    property,
    /**
     * Project the projection of the source object's non-simple property.
     * Projector will use the target object's property type as projection.
     * This is useful to project object hierarchies as projected objects (e.g. use a DTO presentation
     * of the property instead of the original type)
     */
    projection,
    /**
     * Project only a property of the entries of a source object's collection property
     * into a new collection of properties of the source object's non-simple property value.
     * Example: a property of type {@code java.util.List<Address>} can be projected on
     * {@code java.util.List<String>} where the elements of the list are the city names of the addresses from
     * the source object.
     */
    propertyCollection,
    /**
     * Analogue to {@link #projectionCollection} but using the projection instead of a property value.
     */
    projectionCollection
}
