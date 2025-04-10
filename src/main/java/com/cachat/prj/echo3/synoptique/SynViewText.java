package com.cachat.prj.echo3.synoptique;

/**
 * un visuel géométrique simple de type texte
 *
 * @author scachat
 */
public class SynViewText extends SynViewBasic {

    public enum TextAlign {
        LEFT,
        CENTER,
        RIGHT
    }

    /**
     * Contenu
     */
    private String text;
    /**
     * taille
     */
    private int fontSize;
    /**
     * underline
     */
    private boolean underline;
    /**
     * linethrough
     */
    private boolean linethrough;
    /**
     * overline
     */
    private boolean overline;
    /**
     * style
     */
    private String fontStyle;
    /**
     * famille
     */
    private String fontFamily;
    /**
     * alignement
     */
    private TextAlign textAlign;
    /**
     * couleur de surlignage
     */
    private int textBackgroundColor;

    public SynViewText() {
        setSubType(SubType.TEXT);
    }

    public SynViewText(String text) {
        this();
        this.text = text;
    }

    public SynViewText(String text, int fontSize) {
        this();
        this.text = text;
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    public boolean isLinethrough() {
        return linethrough;
    }

    public void setLinethrough(boolean linethrough) {
        this.linethrough = linethrough;
    }

    public boolean isOverline() {
        return overline;
    }

    public void setOverline(boolean overline) {
        this.overline = overline;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public TextAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

}
