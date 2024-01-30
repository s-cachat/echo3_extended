package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.ng.TextFieldEx;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.event.DocumentEvent;
import nextapp.echo.app.event.DocumentListener;

/**
 * un champs date/heure
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>Copyright 2003 SST Informatique
 */
public class DateTimeField extends Grid implements DocumentListener, ActionListener {

    /**
     * lageur totale
     */
    private static final Extent FULL_WIDTH = new Extent(100, Extent.PERCENT);
    /**
     * date
     */
    private DateSelect tfD;
    /**
     * heure
     */
    private TextFieldEx tfH;

    /**
     * Constructeur
     */
    public DateTimeField() {
        this(true);
    }
    /**
     * flag : impose la saisie de l'heure
     */
    private boolean full;

    /**
     * Constructeur
     *
     * @param full si true, impose la saisie de la date et de l'heure, sinon, si
     * l'heure n'est pas saisie, on prend minuit, et le flag gotTime est a false
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public DateTimeField(boolean full) {
        super(2);
        this.full = full;
        setWidth(FULL_WIDTH);
        tfD = new DateSelect();
        tfD.addActionListener(this);
        add(tfD);
        tfH = new TextFieldEx();
        tfH.getDocument().addDocumentListener(this);
        tfH.addActionListener(this);
        add(tfH);
        if (!full) {
            tfD.setSelectedDate(new Date());
        }
    }

    /**
     * fixe le tooltip
     */
    public void setToolTipText(String s) {
        tfD.setToolTipText(s);
        tfH.setToolTipText(s);
    }
    /**
     * drapeau : heure saisie (non gere si full=true)
     */
    private boolean gotTime = true;

    /**
     * drapeau : heure saisie (non gere si full=true)
     */
    public boolean gotTime() {
        return gotTime;
    }

    /**
     * fixe la date
     */
    public void setDate(java.util.Calendar d) {
        tfD.setSelectedDate(d);
        tfH.setText(String.format("%1$tH:%1$tM:%1$tS", d));
    }

    /**
     * fixe la date
     */
    public void setDate(java.util.Date d) {
        if (d == null) {
            tfH.setText("");
        } else {
            tfD.setSelectedDate(d);
            tfH.setText(String.format("%1$tH:%1$tM:%1$tS", d));
        }
    }

    /**
     * lit la date
     */
    public Date getDate() {
        Calendar cal = getCalendar();
        return cal == null ? null : cal.getTime();
    }

    /**
     * lit la date
     */
    public GregorianCalendar getCalendar() {

        gotTime = false;
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(tfD.getSelectedDate());

        Matcher x = hrPat.matcher(tfH.getText());
        if (x.matches()) {
            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(x.group(1)));
            if (x.group(3) != null) {
                cal.set(Calendar.MINUTE, Integer.parseInt(x.group(3)));
            } else {
                cal.set(Calendar.MINUTE, 0);
            }
            if (x.group(5) != null) {
                cal.set(Calendar.SECOND, Integer.parseInt(x.group(5)));
            } else {
                cal.set(Calendar.SECOND, 0);
            }
            gotTime = true;
            tfH.setText(String.format("%1$tH:%1$tM:%1$tS", cal));
            return cal;
        } else if (!full) {
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            tfH.setText("");
            return cal;
        }
        return null;
    }

    /**
     * fixe le style (propage)
     */
    @Override
    public void setStyleName(String name) {
        tfH.setStyleName(name);
    }

    /**
     * fixe le style (bouton)
     */
    public void setButtonStyleName(String name) {
        tfD.setStyleName(name);
    }

    /**
     * traite l'action
     */
    public void documentUpdate(DocumentEvent e) {
        ActionEvent ae = new ActionEvent(this, "TIME");
        for (ActionListener a : listeners) {
            a.actionPerformed(ae);
        }
    }

    /**
     * traite l'action
     */
    public void actionPerformed(ActionEvent e) {
        for (ActionListener a : listeners) {
            a.actionPerformed(e);
        }
    }
    /**
     * les actionListener
     */
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    /**
     * ajoute un actionListener
     */
    public void addActionListener(ActionListener a) {
        listeners.add(a);
    }

    public void removeActionListener(ActionListener a) {
        listeners.remove(a);
    }
    /**
     * pattern pour lire l'heure
     */
    private static java.util.regex.Pattern hrPat = java.util.regex.Pattern.compile("(\\d\\d?)([^\\d](\\d\\d?)([^\\d](\\d\\d?))?)?");
}
