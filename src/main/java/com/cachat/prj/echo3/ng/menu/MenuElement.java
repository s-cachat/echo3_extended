package com.cachat.prj.echo3.ng.menu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author scachat
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso(value = {MenuItem.class, SubMenu.class})
public class MenuElement {

    public MenuElement() {
    }

    public MenuElement(String label, String icon, String permission) {
        this.label = label;
        this.icon = icon;
        this.permission = permission;
    }
    @XmlAttribute(required = true)
    protected String label;

    /**
     * Get the value of label
     *
     * @return the value of label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the value of label
     *
     * @param label new value of label
     */
    public void setLabel(String label) {
        this.label = label;
    }
    @XmlAttribute(required = false)
    protected String icon;

    /**
     * Get the value of icon
     *
     * @return the value of icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set the value of icon
     *
     * @param icon new value of icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @XmlAttribute(required = false)
    protected String permission;

    /**
     * Get the value of permission
     *
     * @return the value of permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the value of permission
     *
     * @param permission new value of permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }
    @XmlAttribute
    protected Integer permissionLevel;

    /**
     * Get the value of permissionLevel
     *
     * @return the value of permissionLevel
     */
    public Integer getPermissionLevel() {
        return permissionLevel;
    }

    /**
     * Set the value of permissionLevel
     *
     * @param permissionLevel new value of permissionLevel
     */
    public void setPermissionLevel(Integer permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
    /**
     * obligation d'etre superadmin
     */
    @XmlAttribute(required = false)
    protected boolean superAdmin;

    /**
     * Get the value of superAdmin
     *
     * @return the value of superAdmin
     */
    public boolean isSuperAdmin() {
        return superAdmin;
    }

    /**
     * Set the value of superAdmin
     *
     * @param superAdmin new value of superAdmin
     */
    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    @XmlAttribute
    protected String license;

    /**
     * Get the value of license
     *
     * @return the value of license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Set the value of license
     *
     * @param license new value of license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    
}
