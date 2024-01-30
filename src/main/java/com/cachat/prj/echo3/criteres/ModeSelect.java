package com.cachat.prj.echo3.criteres;

/**
 * les modes posibles
 * @author scachat
 */
public enum ModeSelect {
   IN_ELEMENTS("? in elements(%s)"), EQUALS("%s=?"), NONE(null),GREATER_THAN("(? > %s)");
   private String q;

   ModeSelect(String q) {
      this.q = q;
   }

   public String getQuery() {
      return q;
   }
   
}
