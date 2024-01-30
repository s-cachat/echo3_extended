package com.cachat.prj.echo3.criteres;

import com.cachat.prj.echo3.components.ButtonEx2;
import java.util.List;

/**
 * un bouton forçant la mise a jour
 */
public class ButtonCrit extends Crit {

    /**
     * le choix
     */
    private ButtonEx2 tf;

    /**
     * constructeur
     *
     * @param cont le conteneur référant
     */
    public ButtonCrit(CritContainer cont) {
        super(cont, null);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        tf = new ButtonEx2(cont.getBaseString("search"));
        tf.setStyleName("Button");
        critf.add(tf);
        tf.addActionListener(cont);
        tf.setToolTipText(cont.getString(prop + ".tt"));
        cont.addCrit(this);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    /**
     * met a jour le where
     *
     * @return le fragment de chaine where
     * @param arg la liste des arguments a completer
     */
    @Override
    public String updateWhere(List<Object> arg) {
        return null;
    }

    @Override
    public String getSummary() {
        return null;
    }
}
