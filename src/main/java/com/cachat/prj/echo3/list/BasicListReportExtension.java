/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cachat.prj.echo3.list;

import java.util.List;
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

/**
 * interface pour une basic liste qui peut extraire sont contenu
 * @author scachat
 */
public interface BasicListReportExtension {
/**
 * génère le rapport
 * @param sb la chaine de sélection (comme pour le update de la liste)
 * @param arg les arguments
 * @return le rapport
 */
   public abstract JasperReportBuilder generate(StringBuilder sb, List<Object> arg);

}
