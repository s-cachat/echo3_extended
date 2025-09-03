package com.cachat.prj.echo3.ng.menu;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author scachat
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "submenu")
public class SubMenu extends MenuElement {
public SubMenu(){

}

   public SubMenu(String label, String icon, String permission) {
      super(label, icon, permission);
   }

   @XmlElement
   protected List<MenuElement> childs;

   /**
    * Get the value of childs
    *
    * @return the value of childs
    */
   public List<MenuElement> getChilds() {
      if (childs == null) {
         childs = new ArrayList<MenuElement>();
      }
      return childs;
   }

   /**
    * Set the value of childs
    *
    * @param childs new value of childs
    */
   public void setChilds(List<MenuElement> childs) {
      this.childs = childs;
   }
}
