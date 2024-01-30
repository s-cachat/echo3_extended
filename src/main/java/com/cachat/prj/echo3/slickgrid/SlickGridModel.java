package com.cachat.prj.echo3.slickgrid;

import com.cachat.util.SimpleJsonWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author scachat
 */
public class SlickGridModel {

    protected final static Logger logger = Logger.getLogger(SlickGridModel.class.getName());
    /**
     * identifiant de la table
     */
    private String id;
    /**
     * les descriptions des colonnes
     */
    private List<SlickGridColumnModel> columns = new ArrayList<>();
    /**
     * les colonnes en lecture seule, qui peuvent être mise à jour par le
     * serveur
     */
    private List<String> roColumns = null;
    /**
     * les données
     */
    private List<SlickGridData> data = new ArrayList<>();
    /**
     * les options
     */
    private SlickGridOptions options = new SlickGridOptions();
    /**
     * la grille
     */
    private SlickGrid grid = null;
    /**
     * les mises à jour par lot
     */
    private List<CellChange> changes = new ArrayList<>();

    public void setGrid(SlickGrid grid) {
        this.grid = grid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SlickGridColumnModel> getColumns() {
        roColumns = null;
        return columns;
    }

    public void setColumns(List<SlickGridColumnModel> columns) {
        this.columns = columns;
        roColumns = null;
    }

    public List< SlickGridData> getData() {
        return data;
    }

    public void setData(List< SlickGridData> data) {
        this.data = data;
    }

    public SlickGridOptions getOptions() {
        return options;
    }

    public void setOptions(SlickGridOptions options) {
        this.options = options;
    }

    public boolean isFieldReadOnly(String field) {
        if (roColumns == null) {
            roColumns = new ArrayList<>();
            columns.stream().filter((c) -> c.editor == null).forEach((c) -> roColumns.add(c.getField()));
        }
        return roColumns.contains(field);
    }

    public void update(int row, String field, Object value) {

        if (isFieldReadOnly(field)) {
            changes.add(new CellChange(row, field, value));
            data.get(row).put(field, value);
            logger.log(Level.FINEST, "DO  updating value for rw column {0}", field);
        } else {
            logger.log(Level.INFO, "not updating value for rw column {0}", field);
        }
    }

    public void commit() {
        grid.setCellChange(SimpleJsonWriter.toString(new CellChanges(changes)));
        changes.clear();
    }
}
