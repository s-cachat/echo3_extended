package com.cachat.prj.echo3.editor;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.util.BeanTools;
import com.cachat.prj.echo3.ng.SelectFieldEx;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.list.DefaultListModel;

/**
 * selecteur de langue
 */
public class BasicEditorMLSelect extends SelectFieldEx implements ActionListener {

    /**
     * les composants impactes
     */
    private List<BasicEditorMLItem> content = new ArrayList<>();
    BasicEditor editor;

    public BasicEditorMLSelect(BasicEditor editor) {
        super();
        this.editor = editor;
        String[] lib = new String[BaseApp.AVAILABLE_LANGUAGES.length];
        for (int i = 0; i < lib.length; i++) {
            lib[i] = BaseApp.AVAILABLE_LANGUAGES[i].getDisplayLanguage(editor.getApp().getLocale());
        }
        setModel(new DefaultListModel(lib));
        setSelectedIndex(0);
        curLoc = editor.getApp().getLocale();
        this.addActionListener(this);
    }

    /**
     * donne le composants impactes
     *
     * @return les composants
     */
    protected List<BasicEditorMLItem> getContent() {
        return content;
    }
    /**
     * locale courante
     */
    private Locale curLoc;

    @Override
    public void actionPerformed(ActionEvent e) {
        Locale newLoc = BaseApp.AVAILABLE_LANGUAGES[getSelectedIndex()];
        boolean isDefaultLoc = newLoc.equals(editor.getApp().getLocale());
        // on enregistre
        save();
        // on met a jour les references
        for (BasicEditorMLItem i : content) {
            // on recupere la table locale vers langue
            Map m = i.getMap();
            if (m != null) {
                try {
                    //on recupere l'objet pour la langue demandee
                    Object x = m.get(curLoc.getLanguage());
                    if (x != null) {
                        if (isDefaultLoc) {
                            i.getLabel().setText("");
                        } else {
                            x = m.get(editor.getApp().getLocale().getLanguage());
                            if (x == null) {
                                i.getLabel().setText("");
                            } else {
                                String v = BeanTools.get(x, i.getPropText());
                                if (v != null) {
                                    i.getLabel().setText(v);
                                } else {
                                    i.getLabel().setText("");
                                }
                            }
                        }
                    }
                } catch (NoSuchElementException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        curLoc = newLoc;
        edite(editor.cur);
    }

    /**
     * réinitialise les champs
     */
    public void endEdite() {
        for (BasicEditorMLItem i : content) {
            i.getComp().setText("");
        }
    }

    public void save() {
        for (BasicEditorMLItem i : content) {
            String val = i.getComp().getText();
            if (val == null || val.trim().length() == 0) {
                val = null;
            }
            Map m = i.getMap();
            if (m != null) {
                try {
                    //on recupere l'objet pour la langue demandee
                    Object x = m.get(curLoc.getLanguage());
                    if (x == null && val != null) {
                        //il n'existe pas, et on a quelque chose a mettre dedans : on le cree
                        x = initLocaleSpecific(i, editor.cur, m, curLoc);
                    }
                    if (x != null) {
                        BeanTools.set(x, i.getPropText(), val);
                    }
                } catch (NoSuchElementException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvocationTargetException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void edite(Object o) {
        for (BasicEditorMLItem i : content) {
            // on recupere la table locale vers langue
            Map m = i.getMap();
            if (m != null) {
                if (m.get(editor.getApp().getLocale().getLanguage()) == null) {
                    initLocaleSpecific(i, editor.cur, m, editor.getApp().getLocale());
                }
                try {
                    //on recupere l'objet pour la langue demandee
                    Object x = m.get(curLoc.getLanguage());
                    if (x == null) {
                        //il n'existe pas, on initialise a vide
                        i.getComp().setText("");
                    } else {
                        String v = BeanTools.get(x, i.getPropText());
                        i.getComp().setText(v == null ? "" : v);
                    }
                } catch (NoSuchElementException ex) {
                    Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Object initLocaleSpecific(BasicEditorMLItem i, Object cur, Map m, Locale loc) {
        try {
            Object x = i.getType().getConstructor().newInstance();
            Class cl = x.getClass();
            for (Method mth : cl.getMethods()) {
                if (mth.getName().startsWith("set")) {
                    if (mth.getParameterTypes().length == 1) {
                        if (mth.getParameterTypes()[0].isAssignableFrom(cur.getClass())) {
                            mth.invoke(x, cur);
                        }
                    }
                }
            }
            m.put(loc.getLanguage(), x);
            //si il a une propriete de type locale, on l'affecte
            for (Method mth : i.getType().getMethods()) {
                if (mth.getName().startsWith("set")) {
                    Class<?>[] tps = mth.getParameterTypes();
                    if (tps.length == 1 && Locale.class.isAssignableFrom(tps[0])) {
                        mth.invoke(x, loc);
                    }
                }
            }
            return x;
        } catch (IllegalArgumentException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(BasicEditor.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
