package com.cachat.prj.echo3.base;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import nextapp.echo.app.*;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.extras.app.CalendarSelect;

/**
 *
 * @author scachat
 */
public class DateSelect extends Button {

    /**
     * la date en cours
     */
    private Date date;
    /**
     * le format de date
     */
    private DateFormat df;
    /**
     * le calendrier
     */
    private CalendarSelect cs;
    /**
     * le popup
     */
    private WindowPane pu;
    /**
     * le bouton ok
     */
    private Button ok;

    /**
     * Constructeur
     */
    public DateSelect() {
        this(new Date());
    }

    /**
     * Constructeur
     *
     * @param date la date initiale
     */
    public DateSelect(Date date) {
        this.date = date;
        ApplicationInstance app = ApplicationInstance.getActive();
        Locale loc;
        if (app != null) {
            loc = app.getLocale();
        } else {
            loc = Locale.getDefault();
        }
        df = DateFormat.getDateInstance(DateFormat.MEDIUM, loc);

        pu = new WindowPane("", new Extent(230), new Extent(240));
        Column col = new Column();
        pu.add(col);
        cs = new CalendarSelect(date);
        col.add(cs);
        ok = new Button("OK");
        ok.addActionListener((e) -> endPopup());
        ok.setLineWrap(false);
        col.add(ok);
        ok.setStyleName("Button");
        pu.setClosable(false);
        pu.setModal(true);
        pu.setResizable(false);
        pu.setTitleHeight(new Extent(0));

        setText(df.format(date));
        super.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                popup();
            }
        });

    }

    @Override
    public void setStyle(Style newValue) {
        super.setStyle(newValue);
        //ok.setStyle(newValue);
    }

    @Override
    public void setStyleName(String newValue) {
        super.setStyleName(newValue);
        //ok.setStyleName(newValue);
    }

    /**
     * action
     */
    public void popup() {
        if (pu.getParent() == null) {
            Component c = getParent();
            while (c != null && (!(c instanceof ContentPane))) {
                c = c.getParent();
            }
            if (c == null) {
                System.out.println("Pas de parent !");
                return;
            }
            ((ContentPane) c).add(pu);
        }
        pu.setVisible(true);
    }

    /**
     * end action
     */
    public void endPopup() {
        date = cs.getDate();
        pu.setVisible(false);
        setText(df.format(date));
        actionPerformed(new ActionEvent(this, "DATE"));
    }

    public Date getSelectedDate() {
        return date;
    }

    /**
     * fixe la date
     *
     * @param date la date sélectionnée
     */
    public void setSelectedDate(Calendar date) {
        setSelectedDate(date == null ? (Date) null : date.getTime());
    }

    /**
     * fixe la date
     *
     * @param date la date sélectionnée
     */
    public void setSelectedDate(Date date) {
        this.date = date;
        if (date != null) {
            cs.setDate(date);
            setText(df.format(date));
        } else {
            setText("");
            cs.setDate(new Date());
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
    @Override
    public void addActionListener(ActionListener a) {
        listeners.add(a);
    }

    @Override
    public void removeActionListener(ActionListener a) {
        listeners.remove(a);
    }
}
