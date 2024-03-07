package com.cachat.prj.echo3.components;

import com.cachat.prj.echo3.outils.LocalResource;
import nextapp.echo.webcontainer.Service;

/**
 * les services génériquesutilisés par plusieurs composants
 * @author scachat
 */
public class GenericServices {
    public static final Service JQUERY_SERVICE = LocalResource.forJsResource(GenericServices.class,"jq-3.7.1", "/com/cachat/prj/echo3/components/js/jquery-3.7.1.min.js");
    public static final Service JQUERY_UI_SERVICE = LocalResource.forJsResource(GenericServices.class,"jquery.ui", "/com/cachat/prj/echo3/components/js/jquery-ui.js");

}
