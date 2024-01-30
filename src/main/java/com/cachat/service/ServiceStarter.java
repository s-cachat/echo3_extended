package com.cachat.service;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 *
 * @author scachat
 */
@WebListener
public class ServiceStarter implements ServletContextListener {

   public static final Logger logger = Logger.getLogger(ServiceStarter.class.getName());

   @Override
   public void contextInitialized(ServletContextEvent sce) {
      try {
         ServiceRegistry.getInstance().startAll();
      } catch (Throwable t) {
         logger.log(Level.SEVERE, null, t);
      }
   }

   @Override
   public void contextDestroyed(ServletContextEvent sce) {
      try {
         ServiceRegistry.getInstance().stopAll();
      } catch (Throwable t) {
         logger.log(Level.SEVERE, null, t);
      }
   }
}
