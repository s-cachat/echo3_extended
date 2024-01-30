package com.cachat.prj.echo3.base;

/**
 * une fenetre permettant de choisir un element dans une liste pour édition. Il
 * s'agit généralement d'une XXXListPane étendant BasicList, et utilisé dans la
 * fenetre de détail de type BasicEditor. Toutefois, on peut remplacer la
 * BasicList dans le cas par exemple des liste de type arbre
 *
 * @author scachat
 * @param TypeObjet le type d'objet traité par le listener
 */
public interface EntityModifiedListener<TypeObjet> {

    /**
     * met a jour la liste suite à une modification dans une fenetre de detail
     */
    public void update(TypeObjet objet);
}
