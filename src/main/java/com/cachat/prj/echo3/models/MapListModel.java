package com.cachat.prj.echo3.models;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * un list model prenant ses valeurs dans une liste d'objets
 *
 * @author SST Informatique &lt;sst@cachat.org&gt;
 * @license protected
 * <p>Copyright 2003 SST Informatique
 */
public class MapListModel<T> extends AbstractRawListModel {

   /**
    * Constructeur
    * @param values les valeurs
    * @param propName le nom de la propriete a utiliser comme libelle
    */
   public MapListModel(LinkedHashMap<T, String> values) {
      this(values,  false);
   }
   /**
    * avec valeur neutre
    */
   private boolean neutre;

   /**
    * Constructeur
    * @param values les valeurs
    * @param propName le nom de la propriete a utiliser comme libelle
    * @param neutre si true, ajoute une valeur neutre en tete
    */
   public MapListModel(LinkedHashMap<T, String> values, boolean neutre) {
      this.neutre = neutre;
      setData(values);
   }

   /**
    * change la liste de donnee
    */
   public void setData(LinkedHashMap<T, String> values) {
      int s1 = this.values == null ? 0 : this.values.size();
      if (neutre) {
         this.keys.add(null);
         this.values.add("");
      }
      for (Map.Entry<T, String> o : values.entrySet()) {
         this.values.add(o.getValue());
         this.keys.add(o.getKey());
      }

      fireContentsChanged(0, Math.max(s1, values.size()));
   }
   /**
    * les valeurs
    */
   private List<String> values = new ArrayList<String>();
   /**
    * les clés
    */
   private List<T> keys = new ArrayList<T>();
  

   /**
    * donne une valeur
    */
   @Override
   public Object get(int i) {
      Object v = i < values.size() ? values.get(i) : null;
      return v == null ? "" : v;
   }

   /**
    * donne une valeur
    */
   @Override
   public T getRaw(int i) {
      T v = i >= 0 && i < keys.size() ? keys.get(i) : null;
      return v;
   }

   /**
    * donne le nombre de valeurs
    */
   @Override
   public int size() {
      return values.size();
   }

   /**
    * donne l'index d'une valeur
    */
   public int indexOf(T value) {
      return values.indexOf(value);
   }
}

