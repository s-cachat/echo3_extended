package com.cachat.service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * un service generique, base sur un thread unique
 * @author scachat
 */
public abstract class AbstractService implements Service, Runnable {

   private final String name;
   private Thread thr;
   /**
    * drapeau indiquant que la boucle principale (dans run) doit fonctionner. Est mis à false par stopService, en plus de l'envoi d'une interruption
    */
   protected boolean running = false;

   public AbstractService(String name) {
      this.name = name;
   }

   @Override
   public void startService() {
      if (thr != null) {
         if (thr.isAlive()) {
            running = false;
            thr.interrupt();
            try {
               thr.join(1000);
            } catch (InterruptedException ex) {
               //nop
            }
         }
         if (thr.isAlive()) {
            throw new RuntimeException("Process " + name + " en cours ou mal arrêté");
         } else {
            thr = null;
         }
      }
      if (thr == null) {
         running = true;
         thr = new Thread(this, name);
         thr.setDaemon(true);
         thr.start();
      } else {
      }
   }

   @Override
   public void stopService() {
      running = false;
      if (thr != null) {
         if (thr.isAlive()) {
            thr.interrupt();
            try {
               thr.join(500);
               thr = null;
            } catch (InterruptedException ex) {
               Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
         } else {
            thr = null;
         }
      }
   }

   @Override
   public boolean isServiceAlive() {
      return thr != null && thr.isAlive();
   }

   @Override
   public String getServiceName() {
      return name;
   }
}
