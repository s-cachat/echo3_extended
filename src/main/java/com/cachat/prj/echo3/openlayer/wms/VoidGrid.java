package com.cachat.prj.echo3.openlayer.wms;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * une map en deux dimensions (ou une table à trous)
 *
 * @author scachat
 * @param D le type des valeurs
 */
public class VoidGrid<D> {

    Map<Integer, Map<Integer, D>> data = new HashMap<>();

    /**
     * fixe une valeur
     *
     * @param x coordonnée x
     * @param y coordonnée y
     * @param v la valeur
     */
    public void put(int x, int y, D v) {
        Map<Integer, D> a = data.get(x);
        if (a == null) {
            a = new HashMap<>();
            data.put(x, a);
        }
        a.put(y, v);
    }

    /**
     * donne une valeur
     *
     * @param x coordonnée x
     * @param y coordonnée y
     * @return la valeur
     */
    public D get(int x, int y) {
        Map<Integer, D> a = data.get(x);
        if (a == null) {
            return null;
        }
        return a.get(y);
    }

    /**
     * donne toutes les valeurs
     *
     * @return un ensemble contenant toutes les valeurs
     */
    public Set<Entry<D>> entrySet() {
        Set<Entry<D>> res = new HashSet<>();
        for (Map.Entry<Integer, Map<Integer, D>> x : data.entrySet()) {
            for (Map.Entry<Integer, D> y : x.getValue().entrySet()) {
                res.add(new Entry(x.getKey(), y.getKey(), y.getValue()));
            }
        }
        return res;
    }

    /**
     * teste si une valeur existe
     *
     * @param x coordonnée x
     * @param y coordonnée y
     * @return true si la valeur existe
     */
    boolean hasValueFor(int x, int y) {
        Map<Integer, D> a = data.get(x);
        if (a == null) {
            return false;
        }
        return a.containsKey(y);
    }

    /**
     * ajoute toutes les valeurs de la grille donnée
     *
     * @param ext les nouvelles valeurs
     */
    void putAll(VoidGrid<D> ext) {
        for (VoidGrid.Entry<D> z : ext.entrySet()) {
            put(z.getX(), z.getY(), z.getValue());
        }
    }

    /**
     * une valeur de la table 2D
     *
     * @param <D> el type des valeurs
     */
    public static class Entry<D> {

        /**
         * coordonnée x
         */
        private final int x;
        /**
         * coordonnée y
         */
        private final int y;
        /**
         * valeur
         */
        public D value;

        public Entry(int x, int y, D value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        public D getValue() {
            return value;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
