package com.cachat.prj.echo3.components;

import nextapp.echo.app.Alignment;
import nextapp.echo.app.Component;
import nextapp.echo.app.Grid;
import nextapp.echo.app.IllegalChildException;
import nextapp.echo.app.Insets;
import nextapp.echo.app.layout.GridLayoutData;

/**
 *
 * @author scachat
 */
public class GridOddAlignRight extends Grid{

    public GridOddAlignRight() {
    }

    public GridOddAlignRight(int size) {
        super(size);
    }

    @Override
    public void add(Component c, int n) throws IllegalChildException {
        super.add(c, n); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(Component c) {
        super.add(c); 
        if ((getComponentCount()&1)!=0){
            GridLayoutData gld=new GridLayoutData();
            gld.setAlignment(Alignment.ALIGN_RIGHT);
            gld.setInsets(new Insets(0, 0, 8, 0));
            c.setLayoutData(gld);
            
        }
    }
    
}
