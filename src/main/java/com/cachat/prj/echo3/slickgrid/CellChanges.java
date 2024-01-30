package com.cachat.prj.echo3.slickgrid;

import java.util.List;

/**
 * batch de modifications
 * @author scachat
 */
public class CellChanges {
    private List<CellChange> changes;

    public CellChanges() {
    }

    public CellChanges(List<CellChange> changes) {
        this.changes = changes;
    }

    public List<CellChange> getChanges() {
        return changes;
    }

    public void setChanges(List<CellChange> changes) {
        this.changes = changes;
    }
}
