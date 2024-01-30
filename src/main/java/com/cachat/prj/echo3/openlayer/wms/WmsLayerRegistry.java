package com.cachat.prj.echo3.openlayer.wms;

import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.http.HttpSession;

/**
 * Le registre des calques WMS
 *
 * @author scachat
 */
public class WmsLayerRegistry {

    private static final WmsLayerRegistry instance = new WmsLayerRegistry();

    /**
     * donne l'instance unique
     *
     * @return l'instance
     */
    public static WmsLayerRegistry getInstance() {
        return instance;
    }

    private WmsLayerRegistry() {
    }
    /**
     * les calques communs à tous les utilisateurs
     */
    private final Map<String, LayerWMS> sharedLayers = new HashMap<>();

    /**
     * donne le calque
     *
     * @param name le nom
     * @return le calque
     */
    public LayerWMS getLayerWMS(String name, HttpSession sess) {
        LayerWMS res;
        res = sharedLayers.get(name);
        //ajouter calques user
        return res;
    }

    /**
     * enregistre un calque commun à tous les utilisateurs
     */
    public void registerSharedLayer(String name, LayerWMS layer) {
        sharedLayers.put(name, layer);
    }
}
