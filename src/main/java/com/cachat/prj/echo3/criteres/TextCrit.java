package com.cachat.prj.echo3.criteres;

import java.util.List;
import nextapp.echo.app.TextField;

/**
 * un critere de type choix 1/n
 */
public class TextCrit extends Crit {

    public static enum TextSearchMode {

        /**
         * egalite stricte
         */
        EQUALS,
        /**
         * comparaison de type like, pas de modification implicite du critère de
         * comparaison
         */
        LIKE,
        /**
         * comparaison de type like, ajout d'un % a la fin du critère
         */
        STARTS_WITH,
        /**
         * comparaison de type like, ajout d'un % au début du critère
         */
        ENDS_WITH,
        /**
         * comparaison de type like, ajout d'un % de part et d'autre du critère
         */
        CONTAINS
    }
    
    /**
     * le choix
     */
    private TextField tf;
    
    /**
     * le mode de recherche
     */
    private final TextSearchMode mode;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere le mode de comparaison est
     * LIKE
     */
    public TextCrit(CritContainer cont, String prop) {
        this(cont, prop, TextSearchMode.LIKE);
    }

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     * @param mode le mode de comparaison
     */
    public TextCrit(CritContainer cont, String prop, TextSearchMode mode) {
        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new TextField();
        tf.setStyleName("Grid");
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
        this.mode = mode;
    }

    /**
     * donne le texte actuellement saisi. permet �ventuellement un pr�
     * traitement du texte
     *
     * @return le texte
     */
    public String getText() {
        return tf.getText();
    }

    /**
     * fixe le texte
     *
     * @param text le texte
     */
    public void setText(String text) {
        tf.setText(text);
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
        if (s != null && s.trim().length() > 0) {
            s = s.trim();
            switch (mode) {
                case CONTAINS:
                    arg.add("%" + s + "%");
                    return String.format("%1$s like ?", prop);
                case ENDS_WITH:
                    arg.add("%" + s);
                    return String.format("%1$s like ?", prop);
                case EQUALS:
                    arg.add(s);
                    return String.format("%1$s = ?", prop);
                case LIKE:
                default:
                    arg.add(s);
                    return String.format("%1$s like ?", prop);
                case STARTS_WITH:
                    arg.add(s + "%");
                    return String.format("%1$s like ?", prop);
            }
        } else {
            return null;
        }
    }

    @Override
    public String getSummary() {
        String s = getText();
        if (s != null && s.trim().length() > 0) {
            s = s.trim();
            return String.format("%1$s %2$s %3$s", cont.getString(prop), cont.getBaseString("TextSearchMode." + mode.toString()), s);
        } else {
            return null;
        }
    }
}
