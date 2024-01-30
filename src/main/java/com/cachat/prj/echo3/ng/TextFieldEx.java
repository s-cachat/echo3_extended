/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.ng;

import com.cachat.prj.echo3.ng.able.Sizeable;
import nextapp.echo.app.TextField;
import nextapp.echo.app.text.Document;
import nextapp.echo.app.text.StringDocument;

/**
 *
 * @author scachat
 */
public class TextFieldEx extends TextField implements Sizeable {

    public TextFieldEx() {
    }

    public TextFieldEx(String s) {
        this(new StringDocument());
        ((StringDocument) getDocument()).setText(s);
    }

    public TextFieldEx(Document document) {
        super(document);
    }

    public TextFieldEx(Document document, String text, int columns) {
        super(document, text, columns);
    }

    public void setRegex(String dddd) {
        //TODO
    }
}
