package org.informagen.echo.app;

import nextapp.echo.app.Component;

public class HtmlComponent extends Component {

    public static final String PROPERTY_HTML = "html";

    public HtmlComponent() {
        this("");
    }

    public HtmlComponent(String html) {
        setHtml(html);
    }

    public String getHtml() {
        return (String) get(PROPERTY_HTML);
    }

    public void setHtml(String newValue) {
        set(PROPERTY_HTML, newValue);
    }
}