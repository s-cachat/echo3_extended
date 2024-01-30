package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.ng.ContainerEx;
import com.cachat.prj.echo3.ng.LabelEx;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;

/**
 * une portlet
 *
 * @author scachat
 */
public class Portlet extends ContainerEx {

    /**
     * la zone principale
     */
    protected ContainerEx mainArea;
    /**
     * le titre
     */
    private LabelEx titleLabel;

    /**
     * change le titre
     *
     * @param title le titre
     */
    public void setTitle(String title) {
        if (title != null) {
            titleLabel.setText(title);
            titleLabel.setVisible(true);
        } else {
            titleLabel.setVisible(false);
        }
    }

    /**
     * constructeur. utilise un style avec une petite fonte pour le texte de la
     * zone principale
     *
     * @param title le titre
     *
     */
    public Portlet(BaseApp app, String title) {
        this(app, title, true);

    }

    /**
     * constructeur
     *
     * @param app l'application
     * @param title le titre
     * @param smallFont si true, utilise un style avec une petite fonte pour le
     * texte de la zone principale
     */
    public Portlet(BaseApp app, String title, boolean smallFont) {
        this(app, title, smallFont, 200, 150);
    }

    /**
     * constructeur
     *
     * @param app l'application
     * @param title le titre
     * @param smallFont si true, utilise un style avec une petite fonte pour le
     * texte de la zone principale
     * @param width la largeur du portlet
     * @param height la hauteur du portlet
     */
    public Portlet(BaseApp app, String title, boolean smallFont, int width, int height) {
        super(null, 0, 0, null, width, height);
        Color gray = new Color(225, 255, 255);
        setStyleName("PortletArea");
        titleLabel = new LabelEx(title);
        titleLabel.setStyleName("PortletTitleL");

        ContainerEx titleArea = new ContainerEx(0, 0, 0, null, null, null);
        titleArea.add(titleLabel);
        titleArea.setStyleName("PortletTitleCE");
        super.add(titleArea);
        mainArea = new ContainerEx(0, 32, 0, 0, null, null);
        mainArea.setStyleName(smallFont ? "PortletZone" : "PortletZone2");
        super.add(mainArea);
    }
    
    @Override
    public void add(Component comp) {
        mainArea.removeAll();
        mainArea.add(comp);
    }

    @Override
    public void setHeight(int newValue) {
        super.setHeight(newValue);
        Component x = getParent();
        while (x!=null && !(x instanceof PortletContainer)){
            x=x.getParent();
        }
        if (x != null && x instanceof PortletContainer) {
            ((PortletContainer) x).updatePortletSize();
        }
    }

    @Override
    public void setHeight(Extent newValue) {
        super.setHeight(newValue);
        Component x = getParent();
        if (x != null && x instanceof PortletContainer) {
            ((PortletContainer) x).updatePortletSize();
        }
    }

    @Override
    public void setWidth(int newValue) {
        super.setWidth(newValue);
        Component x = getParent();
        if (x != null && x instanceof PortletContainer) {
            ((PortletContainer) x).updatePortletSize();
        }
    }

    @Override
    public void setWidth(Extent newValue) {
        super.setWidth(newValue);
        Component x = getParent();
        if (x != null && x instanceof PortletContainer) {
            ((PortletContainer) x).updatePortletSize();
        }
    }

}
