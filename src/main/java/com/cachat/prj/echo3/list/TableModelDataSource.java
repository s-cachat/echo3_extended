package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.models.ListTableModel;
import java.util.*;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * une data source mappée en mémoire
 */
public class TableModelDataSource implements JRRewindableDataSource {

    List<Integer> columnNos = new ArrayList<>();
    private int curRow = 0;
    private final ListTableModel lm;

    public TableModelDataSource(ListTableModel lm) {
        for (int i = 0; i < lm.getColumnCount(); i++) {
            if (lm.getColumnClass(i).isAssignableFrom(String.class)) {
                columnNos.add(i);
            }
        }
        this.lm = lm;
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        Integer r = Integer.parseInt(field.getName());
        return lm.getValueAt(r, curRow);
    }

    @Override
    public boolean next() throws JRException {
        return ++curRow < lm.getRowCount();
    }

    @Override
    public void moveFirst() throws JRException {
        curRow = 0;
    }

    public TextColumnBuilder<String>[] getColumns() {
        TextColumnBuilder<String> res[] = new TextColumnBuilder[columnNos.size()];
        for (int i = 0; i < columnNos.size(); i++) {
            res[i] = col.column(lm.getColumnName(columnNos.get(i)), String.valueOf(columnNos.get(i)), type.stringType());
        }
        return res;
    }
}
