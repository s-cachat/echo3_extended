package com.cachat.prj.echo3.ng;

import static com.cachat.prj.echo3.ng.able.Heightable.PROPERTY_HEIGHT;
import com.cachat.prj.echo3.ng.able.Positionable;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_LEFT;
import static com.cachat.prj.echo3.ng.able.Positionable.PROPERTY_RIGHT;
import com.cachat.prj.echo3.ng.able.ScrollBarProperties;
import com.cachat.prj.echo3.ng.able.Scrollable;
import nextapp.echo.app.Alignment;
import nextapp.echo.app.BorderedComponent;
import nextapp.echo.app.Color;
import nextapp.echo.app.Component;
import nextapp.echo.app.Extent;
import nextapp.echo.app.FillImage;
import nextapp.echo.app.FillImageBorder;
import nextapp.echo.app.Insets;
import static nextapp.echo.app.Label.PROPERTY_TEXT_ALIGNMENT;
import nextapp.echo.app.PaneContainer;
import nextapp.echo.app.Position;
import static nextapp.echo.app.Position.ABSOLUTE;
import static nextapp.echo.app.Position.STATIC;

/**
 *
 * @author scachat
 */
public class ContainerEx extends BorderedComponent implements PaneContainer, Positionable, Scrollable {

    public static final String PROPERTY_VERTICAL_SCROLL_POSITION = "verticalScrollPosition";
    public static final String PROPERTY_BACKGROUND_IMAGE = "backgroundImage";
    public static final String PROPERTY_BORDER_IMAGE = "borderImage";
    public static final String TEXT_ALIGNMENT = "textAlignment";
    public static final String PROPERTY_FLEX_BASIS = "flexBasis";
    public static final String PROPERTY_FLEX_GROW = "flexGrow";
    public static final String PROPERTY_FLEX_SHRINK = "flexShrink";
    public static final String PROPERTY_FLEX_WRAP = "flexWrap";
    public static final String PROPERTY_FLEX_DIRECTION = "flexDirection";
    public static final String PROPERTY_RESPONSIVE_WIDTH = "responsiveWidth";
    public static final String PROPERTY_SHADOW = "shadow";
    public static final String PROPERTY_JUSTIFY_CONTENT = "justifyContent";

    public static String FLEX_BASIS_AUTO = "auto";
    public static String FLEX_DIRECTION_ROW = "row";
    public static String FLEX_DIRECTION_ROW_REVERSE = "row-reverse";
    public static String FLEX_DIRECTION_COLUMN = "column";
    public static String FLEX_DIRECTION_COLUMN_REVERSE = "column-reverse";
    public static String FLEX_DIRECTION_RESPONSIVE = "responsive";
    public static String FLEX_WRAP_NOWRAP = "nowrap";
    public static String FLEX_WRAP_WRAP = "wrap";
    public static String FLEX_WRAP_REVERSE = "wrap-reverse";

    /**
     * construteur
     */
    public ContainerEx() {
    }

    /**
     * constructeur
     *
     * @param c composant a ajouter
     */
    public ContainerEx(Component c) {
        super.add(c);
    }

    /**
     * Constructor. position sera absolute, et les 4 distances par rapport au
     * conteneur seront définies
     *
     * @param left extent en pixel
     * @param top extent en pixel
     * @param right extent en pixel
     * @param bottom extent en pixel
     * @param c composant a ajouter
     */
    public ContainerEx(Integer left, Integer top, Integer right, Integer bottom, Component c) {
        super.add(c);
        setBounds(left, top, right, bottom, null, null);
    }

    /**
     * Constructeur. position sera absolute
     *
     * @param left extent en pixel, ou null si indéfini
     * @param top extent en pixel, ou null si indéfini
     * @param right extent en pixel, ou null si indéfini
     * @param bottom extent en pixel, ou null si indéfini
     * @param width extent en pixel, ou null si indéfini
     * @param height extent en pixel, ou null si indéfini
     */
    public ContainerEx(Integer left, Integer top, Integer right, Integer bottom, Integer width, Integer height) {
        setBounds(left, top, right, bottom, width, height);
    }

    /**
     * Constructeur. position sera absolute
     *
     * @param left extent en pixel, ou null si indéfini
     * @param top extent en pixel, ou null si indéfini
     * @param right extent en pixel, ou null si indéfini
     * @param bottom extent en pixel, ou null si indéfini
     * @param width extent en pixel, ou null si indéfini
     * @param height extent en pixel, ou null si indéfini
     * @param comp composant a ajouter
     */
    public ContainerEx(Integer left, Integer top, Integer right, Integer bottom, Integer width, Integer height, Component comp) {
        setBounds(left, top, right, bottom, width, height);
        super.add(comp);
    }

    /**
     * Fixe les limites. position sera absolute
     *
     * @param left extent en pixel, ou null si indéfini
     * @param top extent en pixel, ou null si indéfini
     * @param right extent en pixel, ou null si indéfini
     * @param bottom extent en pixel, ou null si indéfini
     * @param width extent en pixel, ou null si indéfini
     * @param height extent en pixel, ou null si indéfini
     */
    public void setBounds(Integer left, Integer top, Integer right, Integer bottom, Integer width, Integer height) {
        setPosition(ABSOLUTE);
        if (top != null) {
            setTop(new Extent(top));
        }
        if (left != null) {
            setLeft(new Extent(left));
        }
        if (right != null) {
            setRight(new Extent(right));
        }
        if (bottom != null) {
            setBottom(new Extent(bottom));
        }
        if (width != null) {
            setWidth(new Extent(width));
        }
        if (height != null) {
            setHeight(new Extent(height));
        }
    }

    public static final String PROPERTY_INSETS = "insets";

    @Override
    public void clear() {
        //TODO
    }

    @Override
    public Extent getBottom() {
        return (Extent) get(PROPERTY_BOTTOM);
    }

    @Override
    public Extent getLeft() {
        return (Extent) get(PROPERTY_LEFT);
    }

    @Override
    public Position getPosition() {
        return (Position) get(PROPERTY_POSITION);
    }

    @Override
    public Extent getRight() {
        return (Extent) get(PROPERTY_RIGHT);
    }

    @Override
    public Extent getTop() {
        return (Extent) get(PROPERTY_TOP);
    }

    @Override
    public int getZIndex() {
        return (Integer) get(PROPERTY_Z_INDEX);
    }

    @Override
    public boolean isPositioned() {
        return getPosition() != STATIC;
    }

    @Override
    public void setPosition(Position newValue) {
        set(PROPERTY_POSITION, newValue);
    }

    @Override
    public void setBottom(Extent newValue) {
        set(PROPERTY_BOTTOM, newValue);
    }

    @Override
    public void setLeft(Extent newValue) {
        set(PROPERTY_LEFT, newValue);
    }

    @Override
    public void setRight(Extent newValue) {
        set(PROPERTY_RIGHT, newValue);
    }

    @Override
    public void setTop(Extent newValue) {
        set(PROPERTY_TOP, newValue);
    }

    public void setBottom(int newValue) {
        set(PROPERTY_BOTTOM, new Extent(newValue));
    }

    public void setLeft(int newValue) {
        set(PROPERTY_LEFT, new Extent(newValue));
    }

    public void setRight(int newValue) {
        set(PROPERTY_RIGHT, new Extent(newValue));
    }

    public void setTop(int newValue) {
        set(PROPERTY_TOP, new Extent(newValue));
    }

    @Override
    public void setZIndex(int newValue) {
        set(PROPERTY_Z_INDEX, newValue);
    }

    @Override
    public int getScrollBarPolicy() {
        return (Integer) get(PROPERTY_SCROLL_BAR_POLICY);
    }

    @Override
    public Color getScrollBarBaseColor() {
        return (Color) get(PROPERTY_SCROLL_BAR_BASE_COLOR);
    }

    @Override
    public ScrollBarProperties getScrollBarProperties() {
        return (ScrollBarProperties) get(PROPERTY_SCROLL_BAR_PROPERTIES);
    }

    @Override
    public void setScrollBarPolicy(int newValue) {
        set(PROPERTY_SCROLL_BAR_POLICY, newValue);
    }

    @Override
    public void setScrollBarBaseColor(Color newValue) {
        set(PROPERTY_SCROLL_BAR_BASE_COLOR, newValue);
    }

    @Override
    public void setScrollBarProperties(ScrollBarProperties newValue) {
        set(PROPERTY_SCROLL_BAR_PROPERTIES, newValue);
    }

    @Override
    public Extent getWidth() {
        return (Extent) get(PROPERTY_WIDTH);
    }

    @Override
    public void setWidth(Extent newValue) {
        set(PROPERTY_WIDTH, newValue);
    }

    public void setWidth(int newValue) {
        set(PROPERTY_WIDTH, new Extent(newValue));
    }

    @Override
    public Extent getHeight() {
        return (Extent) get(PROPERTY_HEIGHT);
    }

    @Override
    public void setHeight(Extent newValue) {
        set(PROPERTY_HEIGHT, newValue);
    }

    public void setHeight(int newValue) {
        set(PROPERTY_HEIGHT, new Extent(newValue));
    }

    public void setInsets(Insets newValue) {
        set(PROPERTY_INSETS, newValue);
    }

    public Insets getInsets() {
        return (Insets) get(PROPERTY_INSETS);
    }

    public Extent getVerticalScroll() {
        return (Extent) get(PROPERTY_VERTICAL_SCROLL_POSITION);
    }

    public void setVerticalScroll(Extent newValue) {
        set(PROPERTY_VERTICAL_SCROLL_POSITION, newValue);
    }

    public FillImage getBackgroundImage() {
        return (FillImage) get(PROPERTY_BACKGROUND_IMAGE);
    }

    public void setBackgroundImage(FillImage newValue) {
        set(PROPERTY_BACKGROUND_IMAGE, newValue);
    }

    public FillImageBorder getBorderImage() {
        return (FillImageBorder) get(PROPERTY_BORDER_IMAGE);
    }

    public void setBorderImage(FillImageBorder newValue) {
        set(PROPERTY_BORDER_IMAGE, newValue);
    }

    /**
     * Sets the alignment of the text relative to the icon. Note that only one
     * of the provided <code>Alignment</code>'s settings should be non-default.
     *
     * @param newValue the new text position
     */
    public void setTextAlignment(Alignment newValue) {
        set(PROPERTY_TEXT_ALIGNMENT, newValue);
    }

    public void setFlexBasis(String newValue) {
        set(PROPERTY_FLEX_BASIS, newValue);
    }

    /**
     * définie la css flexDirection. note : la valeur "responsive" sera rendue
     * en row si la largeur de la fenêtre dépasse responsiveWidth, et en ligne
     * sinon.
     *
     * @param newValue la valeur
     */
    public void setFlexDirection(String newValue) {
        set(PROPERTY_FLEX_DIRECTION, newValue);
    }

    /**
     * définie la limite de largeur de fenêtre pour le basculement de
     * flexDirection
     *
     * @param newValue la valeur
     */
    public void setResponsiveWidth(Double newValue) {
        set(PROPERTY_RESPONSIVE_WIDTH, newValue);
    }

    public void setFlexGrow(Double newValue) {
        set(PROPERTY_FLEX_GROW, newValue);
    }

    public void setFlexShrink(Double newValue) {
        set(PROPERTY_FLEX_SHRINK, newValue);
    }

    public void setFlexWrap(String newValue) {
        set(PROPERTY_FLEX_WRAP, newValue);
    }

    public String getFlexBasis() {
        return (String) get(PROPERTY_FLEX_BASIS);
    }

    /**
     * donne la css flexDirection. note : la valeur "responsive" sera rendue en
     * row si la largeur de la fenêtre dépasse responsiveWidth, et en ligne
     * sinon.
     *
     * @return la valeur
     */
    public String getFlexDirection() {
        return (String) get(PROPERTY_FLEX_DIRECTION);
    }

    /**
     * donne la limite de largeur de fenêtre pour le basculement de
     * flexDirection
     *
     * @return la valeur
     */
    public Double getResponsiveWidth() {
        return (Double) get(PROPERTY_RESPONSIVE_WIDTH);
    }

    public Double getFlexGrow() {
        return (Double) get(PROPERTY_FLEX_GROW);
    }

    public Double getFlexShrink() {
        return (Double) get(PROPERTY_FLEX_SHRINK);
    }

    public String getFlexWrap() {
        return (String) get(PROPERTY_FLEX_WRAP);
    }

    public void setShadow(String newValue) {
        set(PROPERTY_SHADOW, newValue);
    }

    public String getShadow() {
        return (String) get(PROPERTY_SHADOW);
    }

    public void setJustifyContent(String newValue) {
        set(PROPERTY_JUSTIFY_CONTENT, newValue);
    }

    public String getJustifyContent() {
        return (String) get(PROPERTY_JUSTIFY_CONTENT);
    }
}
