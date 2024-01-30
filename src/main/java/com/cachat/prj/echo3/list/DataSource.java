package com.cachat.prj.echo3.list;

import java.util.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

/**
 * une data source mappée en mémoire
 */
public class DataSource implements JRRewindableDataSource {

    protected String[] columns;
    private List<Map<String, Object>> values;
    private Iterator<Map<String, Object>> iterator;
    private Map<String, Object> currentRecord;

    public DataSource(String... columns) {
        this.columns = columns;
        this.values = new ArrayList<>();
    }

    public void add(Object... values) {
        Map<String, Object> row = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            row.put(columns[i], values[i]);
        }
        this.values.add(row);
    }

    @Override
    public Object getFieldValue(JRField field) throws JRException {
        return currentRecord.get(field.getName());
    }

    @Override
    public boolean next() throws JRException {
        if (iterator == null) {
            this.iterator = values.iterator();
        }
        boolean hasNext = iterator.hasNext();
        if (hasNext) {
            currentRecord = iterator.next();
        }
        return hasNext;
    }

    @Override
    public void moveFirst() throws JRException {
        this.iterator = null;
    }
}
