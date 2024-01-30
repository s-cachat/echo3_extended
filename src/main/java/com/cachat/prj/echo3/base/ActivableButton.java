package com.cachat.prj.echo3.base;

import com.cachat.prj.echo3.interfaces.Activable;
import com.cachat.util.ACEntityManager;
import com.cachat.util.EntityManagerUtil;

/**
 * un bouton dont l'etat commande l'activation d'un Activable
 */
public class ActivableButton extends LineAction {

    /**
     * l'objet
     */
    public Activable x;

    /**
     * Constructeur
     *
     * @param app l'application
     * @param x l'objet concerne
     */
    public ActivableButton(BaseApp app, Activable x) {
        super();
        this.app = app;
        addAction((e) -> action());
        this.x = x;
        update();
    }

    @Override
    protected void update() {
        this.iconImage = app.getStyles().getIcon(x.getActif() ? "add" : "delete");
        this.label = app.getBaseString(x.getActif() ? "desactiver" : "activer");
        super.update();
    }

    /**
     * reagit
     *
     * @return true si l'action est effectuée
     */
    public boolean action() {
        try (ACEntityManager em = EntityManagerUtil.getACEntityManager(app.getEntityManagerName())) {
            em.getTransaction().begin();
            x = (Activable) EntityManagerUtil.refresh(em, x);
            x.setActif(!x.getActif());
            em.merge(x);
            em.getTransaction().commit();
        }
        update();
        return true;
    }
}
