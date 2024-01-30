package com.cachat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Le registre de tous les services
 *
 * @author scachat
 */
public class ServiceRegistry {

    private final static Logger logger = Logger.getLogger(ServiceRegistry.class.getName());
    private static ServiceRegistry instance;

    static {
        getInstance();
    }

    public static synchronized ServiceRegistry getInstance() {
        if (instance == null) {
            instance = new ServiceRegistry();
        }
        return instance;
    }

    void startAll() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName("Service STARTER");
        try {

            services.values().forEach((s) -> {
                if (!s.isServiceAlive() && isAutoStart(s.getServiceName())) {
                    logger.log(Level.SEVERE, "Starting service {0}", s.getServiceName());
                    s.startService();
                    logger.log(Level.SEVERE, "Service {0} started", s.getServiceName());
                } else {
                    logger.log(Level.SEVERE, "Service {0} not in auto start", s.getServiceName());
                }
            });
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    void stopAll() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName("Service STOPPER");
        try {
            for (Service s : services.values()) {
                if (s.isServiceAlive()) {
                    logger.log(Level.SEVERE, "Stopping service {0}", s.getServiceName());
                    s.stopService();
                    logger.log(Level.SEVERE, "Service {0} stopped", s.getServiceName());
                } else {
                    logger.log(Level.SEVERE, "Service {0} not running", s.getServiceName());
                }
            }
        } finally {
            Thread.currentThread().setName(oldName);
        }
    }

    private ServiceRegistry() {
    }
    private final Map<String, Service> services = new TreeMap<>();

    public final void register(Service service) {
        if (service.getServiceName() == null || service.getServiceName().contains(",")) {
            throw new RuntimeException("nom de service invalide : " + service.getServiceName());
        }
        services.put(service.getServiceName(), service);
        if (isAutoStart(service.getServiceName())) {
            service.startService();
        }
    }

    public synchronized List<String> serviceNames() {
        return new ArrayList<>(services.keySet());
    }

    /**
     * lance le service (sauf si il est alive)
     *
     * @param name le nom du service
     */
    public void start(String name) {
        if (!services.containsKey(name)) {
            throw new NoSuchElementException(name);
        }
        Service s = services.get(name);
        if (!s.isServiceAlive()) {
            logger.log(Level.SEVERE, "Starting service {0}", s.getServiceName());
            s.startService();
            logger.log(Level.SEVERE, "Service {0} started", s.getServiceName());
        } else {
            logger.log(Level.SEVERE, "Service {0} already alive", s.getServiceName());
        }
    }

    /**
     * arrête le service
     *
     * @param name le nom du service
     */
    public void stop(String name) {
        if (!services.containsKey(name)) {
            throw new NoSuchElementException(name);
        }
        Service s = services.get(name);
        if (s.isServiceAlive()) {
            logger.log(Level.SEVERE, "Stopping service {0}", s.getServiceName());
            s.stopService();
            logger.log(Level.SEVERE, "Service {0} stopped", s.getServiceName());
        } else {
            logger.log(Level.SEVERE, "Service {0} not running", s.getServiceName());
            s.stopService();
        }
    }

    /**
     * indique si le service est en vie
     *
     * @param name le nom du service
     * @return true si il est en vie
     */
    public boolean isAlive(String name) {
        if (!services.containsKey(name)) {
            throw new NoSuchElementException(name);
        }
        return services.get(name).isServiceAlive();
    }

    /**
     * indique si le service est en mode auto start. l'implémentation par défaut
     * répond toujours oui
     *
     * @param name le nom du service
     * @return true si il est démarré au lancement du serveur
     */
    public boolean isAutoStart(String name) {
        return true;
    }

    /**
     * défini le mode auto start pour le service. l'implémentation par défaut ne
     * gère pas la possibilité pour un service de ne pas être en autostart.
     *
     * @param name le nom du service
     * @param etat true si il doit être démarré au lancement du serveur
     */
    public void setAutoStart(String name, boolean etat) {
        //nop
    }

    /**
     * fourni le service demandé
     *
     * @param name le nom du service
     * @return le service
     */
    public Service getService(String name) {
        if (!services.containsKey(name)) {
            throw new NoSuchElementException(name);
        }
        return services.get(name);
    }

    /**
     * fourni le service demandé
     *
     * @param clazz la classe du service
     * @return le service
     */
    public <T> T getService(Class<T> clazz) {
        for (Service s : services.values()) {
            if (clazz.isInstance(s)) {
                return (T) s;
            }
        }
        return null;
    }
}
