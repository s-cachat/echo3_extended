package com.cachat.prj.echo3.flotchart;

/**
 *
 * @author scachat
 */
public class ChartTooltip {

    String content;
    String xDateFormat;
    String yDateFormat;
    boolean defaultTheme = true;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getxDateFormat() {
        return xDateFormat;
    }

    public void setxDateFormat(String xDateFormat) {
        this.xDateFormat = xDateFormat;
    }

    public String getyDateFormat() {
        return yDateFormat;
    }

    public void setyDateFormat(String yDateFormat) {
        this.yDateFormat = yDateFormat;
    }

    public boolean isDefaultTheme() {
        return defaultTheme;
    }

    public void setDefaultTheme(boolean defaultTheme) {
        this.defaultTheme = defaultTheme;
    }
}
