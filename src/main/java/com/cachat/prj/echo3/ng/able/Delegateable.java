package com.cachat.prj.echo3.ng.able;

/**
 * <code>Delegateable</code> is an interface where a something can act as a
 * Delegate for a Component and hence get and set properties into the Component.
 */
public interface Delegateable {

    /**
     * Retutns the rendered value of a property.
     *
     * @param propertyName - the named of as property as defined by this
     * interface
     * @return the rendered value of a property
     */
    public Object getRenderProperty(String propertyName);

    /**
     * Returns the rendered value of a property or a default value if its found
     * to be null.
     *
     * @param propertyName - the named of as property as defined by this
     * interface
     * @param defaultValue - the default value if the property is null
     *
     * @return the rendered value of a property or a default value if its found
     * to be null
     */
    public Object getRenderProperty(String propertyName, Object defaultValue);
}
