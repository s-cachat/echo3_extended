package com.cachat.service;

/**
 * interface pour un service
 * @author scachat
 */
public interface Service {

   /**
    * demarre le service. Ne fait rien si il est déjà démarré
    */
   public void startService();

   /**
    * arrête le service. Ne fait rien si il n'est pas démarré
    */
   public void stopService();

   /**
    * indique si le service est vivant
    */
   public boolean isServiceAlive();
   /**
    * donne le nom du service
    */
   public String getServiceName();
}
