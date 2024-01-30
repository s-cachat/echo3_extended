package com.cachat.prj.echo3.openlayer.wms;

/**
 * utilitaire pour les voidGrid
 * @author scachat
 */
public class VoidGridUtils {

   /**
    * etend la grille, en ajoutant des cases connexes aux valeurs existantes.
    * Les nouvelles cellules sont ajoutée à data.
    * @param data
    */
   public static void grow(VoidGrid<Double> data) {
      data.putAll(getExtension(data));
   }

   /**
    * étend une cellule
    * @param data les données
    * @param ext la cible
    * @param x x
    * @param y y
    */
   private static void growAdd(VoidGrid<Double> data,
      VoidGrid<Double> ext, int x, int y) {
      if (!data.hasValueFor(x, y)) {
         double s = 0;
         double n = 0;
         Double a;
         a = data.get(x + 1, y);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x + 1, y + 1);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x, y + 1);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x - 1, y + 1);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x - 1, y);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x - 1, y - 1);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x, y - 1);
         if (a != null) {
            s += a;
            n++;
         }
         a = data.get(x + 1, y - 1);
         if (a != null) {
            s += a;
            n++;
         }
         ext.put(x, y, s / n);
      }
   }

   /**
    * etend la grille, en ajoutant des cases connexes aux valeurs existantes.
    * Les nouvelles cellules ne sont pas ajoutée à data.
    * @param data les données
    * @return les nouvelles cellules
    */
   public static VoidGrid<Double> getExtension(VoidGrid<Double> data) {
      VoidGrid<Double> ext = new VoidGrid<Double>();
      for (VoidGrid.Entry<Double> z : data.entrySet()) {
         growAdd(data, ext, z.getX() + 1, z.getY());
         growAdd(data, ext, z.getX() + 1, z.getY() + 1);
         growAdd(data, ext, z.getX(), z.getY() + 1);
         growAdd(data, ext, z.getX() - 1, z.getY() + 1);
         growAdd(data, ext, z.getX() - 1, z.getY());
         growAdd(data, ext, z.getX() - 1, z.getY() - 1);
         growAdd(data, ext, z.getX(), z.getY() - 1);
         growAdd(data, ext, z.getX() + 1, z.getY() - 1);
      }
      return ext;
   }
}
