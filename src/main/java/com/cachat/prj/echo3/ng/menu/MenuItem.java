package com.cachat.prj.echo3.ng.menu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * un menu correspondant une action
 *
 * @author scachat
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class MenuItem extends MenuElement {

    public MenuItem() {
    }

    public MenuItem(String label, String icon, String permission, String id, String newPane) {
        super(label, icon, permission);
        this.id = id;
        this.newPane = newPane;
    }
    @XmlID
    @XmlAttribute(required = true)
    protected String id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * si false, le menu n'est pas généré (par exemple pour la page d'accueil d'un sous menu)
     */
    @XmlAttribute
    private boolean menu=true;
    /**
     * class name of the new WindowPane to display
     */
    @XmlAttribute
    protected String newPane;

    /**
     * Get the value of newPane
     *
     * @return the value of newPane
     */
    public String getNewPane() {
        return newPane;
    }

    /**
     * Set the value of newPane
     *
     * @param newPane new value of newPane
     */
    public void setNewPane(String newPane) {
        this.newPane = newPane;
    }
    /**
     * url of the new Document to open
     */
    @XmlAttribute
    protected String newWindow;

    /**
     * Get the value of newWindow
     *
     * @return the value of newWindow
     */
    public String getNewWindow() {
        return newWindow;
    }

    /**
     * Set the value of newWindow
     *
     * @param newWindow new value of newWindow
     */
    public void setNewWindow(String newWindow) {
        this.newWindow = newWindow;
    }

    public boolean getMenu() {
        return menu;
    }

    public void setMenu(boolean menu) {
        this.menu = menu;
    }

}
