package com.cachat.prj.echo3.criteres;

import java.util.Arrays;
import java.util.List;
import nextapp.echo.app.Component;

/**
 * un critère bidon, permettant d'afficher des composants dans la zone de
 * criteres
 *
 * @author scachat
 */
public class DummyCrit extends Crit {

    public DummyCrit(CritContainer cont, Integer height, Component... comps) {
        super(cont, null);
        if (height != null) {
            cont.extendCritAreaHeight(height);
        }
        critf.addAll(Arrays.asList(comps));
        cont.addCrit(this);
    }

    @Override
    public String updateWhere(List<Object> arg) {
        return null;
    }

    @Override
    public String getSummary() {
        return null;
    }
}
