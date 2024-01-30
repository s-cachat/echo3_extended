package com.cachat.prj.echo3.base.tables;

import java.util.Arrays;
import java.util.List;

import com.cachat.prj.echo3.ng.table.*;
import nextapp.echo.app.Table;
import nextapp.echo.app.*;
import nextapp.echo.app.event.*;

/**
 * Un controleur de table pageable
 *
 * @author Jason Dalton, S. Cachat
 */
public class MyPageableTableNavigation extends Row implements TableModelListener {
    
    private final PageableTableModel model;
    private static final Object[] ROWS_PER_PAGE_OPTIONS = new String[]{"10", "25", "50", "100"};
    private static final List ROWS_PER_PAGE_LIST = Arrays.asList(ROWS_PER_PAGE_OPTIONS);
    
    public MyPageableTableNavigation(Table table) {
        this.model = (PageableTableModel) table.getModel();
        this.model.addTableModelListener(this);
    }

    /**
     * adapte les listes au modele
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        int page = getModel().getCurrentPage();
        if (getModel().getRowCount()<=0 && page>0){
            getModel().setCurrentPage(0);
        }
        reset();
    }

    /**
     * @see nextapp.echo2.app.Component#init()
     */
    @Override
    public void init() {
        super.init();
        reset();
    }
    
    protected void doLayout() {
        setCellSpacing(new Extent(10));
        add(getPreviousButton());
        add(getResultsPerPageSelect());
        add(getPageLabel());
        add(getPageSelect());
        add(getPageCountLabel());
        add(getNextButton());
    }
    
    protected void reset() {
        removeAll();
        doLayout();
        if (model.getCurrentPage() * model.getRowsPerPage() > model.getTotalRows()) {
            model.toPagedViewRowIndex(Math.max(0, model.getTotalRows() - 1));
        }
    }
    
    protected PageableTableModel getModel() {
        return model;
    }
    
    private SelectField getPageSelect() {
        String[] pages = new String[model.getTotalPages()];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = "" + (i + 1);
        }
        
        SelectField select = new SelectField(pages);
        select.setSelectedIndex(model.getCurrentPage());
        select.addActionListener(getPageSelectListener());
        select.setStyleName("DefaultSF");
        return select;
    }
    
    private ActionListener getPageSelectListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectField select = (SelectField) e.getSource();
                int selected = select.getSelectedIndex();
                model.setCurrentPage(selected);
                reset();
            }
        };
    }
    
    private Label getPageLabel() {
        Label label = new Label(" Page ");
        label.setStyleName("DefaultL");
        return label;
    }
    
    private Label getPageCountLabel() {
        Label label = new Label();
        label.setText(" / " + (model.getTotalPages()) + " ");
        label.setStyleName("DefaultL");
        setPageCountLabelText();
        return label;
    }
    
    private void setPageCountLabelText() {
    }
    
    private Button getPreviousButton() {
        Button previousButton = new Button(" < ");
        previousButton.addActionListener(getPreviousListener());
        previousButton.setStyleName("Button");
        return previousButton;
    }
    
    private SelectField getResultsPerPageSelect() {
        SelectField resultsPerPage = new SelectField(ROWS_PER_PAGE_OPTIONS);
        resultsPerPage.addActionListener(getRowsPerPageListener());
        int index = ROWS_PER_PAGE_LIST.indexOf("" + model.getRowsPerPage());
        resultsPerPage.setSelectedIndex(index);
        resultsPerPage.setStyleName("DefaultSF");
        return resultsPerPage;
    }
    
    private Button getNextButton() {
        Button previousButton = new Button(" > ");
        previousButton.addActionListener(getNextListener());
        previousButton.setStyleName("Button");
        return previousButton;
    }
    
    private ActionListener getPreviousListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getModel().getCurrentPage() > 0) {
                    getModel().setCurrentPage(getModel().getCurrentPage() - 1);
                    reset();
                }
            }
        };
    }
    
    private ActionListener getNextListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentPage = getModel().getCurrentPage();
                int maxPage = getModel().getTotalPages();
                if (currentPage + 1 < maxPage) {
                    getModel().setCurrentPage(currentPage + 1);
                    reset();
                }
            }
        };
    }
    
    private ActionListener getRowsPerPageListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelectField select = (SelectField) e.getSource();
                Integer selected = Integer.valueOf((String) select.getSelectedItem());
                getModel().setRowsPerPage(selected);
                getModel().setCurrentPage(0);
                reset();
            }
        };
    }
}
