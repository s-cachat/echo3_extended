package com.cachat.prj.echo3.criteres;

import java.util.List;
import nextapp.echo.app.TextField;

/**
 * un critere de type choix sur nombre
 */
public class LongCrit extends Crit {

    /**
     * le choix
     */
    private final TextField tf;

    /**
     * constructeur
     *
     * @param cont le container de critère
     * @param prop le nom de la propriete critere
     */
    public LongCrit(CritContainer cont, String prop) {

        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new TextField();
        tf.setStyleName("Grid");
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    /**
     * donne le texte actuellement saisi. permet �ventuellement un pr�
     * traitement du texte
     *
     * @return le texte choisi
     */
    public String getText() {
        return tf.getText();
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        String s = getText();
        if (s != null && s.trim().matches("\\d+")) {
            arg.add(Long.valueOf(s.trim()));
            return String.format("%1$s = ?", prop);

        } else {
            return null;
        }
    }

    /**
     * fixe le texte
     *
     * @param text le texte
     */
    public void setText(String text) {
        tf.setText(text);
    }

    @Override
    public String getSummary() {
        String s = getText();
        if (s != null && s.trim().matches("\\d+")) {
            return String.format("%1$s = %2$d", cont.getString(prop), Long.valueOf(s.trim()));
        } else {
            return null;
        }
    }
}
