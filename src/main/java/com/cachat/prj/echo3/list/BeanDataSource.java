package com.cachat.prj.echo3.list;

import com.cachat.prj.echo3.base.LocalisedItem;
import com.cachat.util.BeanTools;
import java.util.Collection;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 *
 * @author scachat
 */
public class BeanDataSource<T> extends DataSource {

   public BeanDataSource(String... columns) {
      super(columns);
   }

   public void add(T bean) {
      Object obj[] = new Object[columns.length];
      for (int i = 0; i < columns.length; i++) {
         obj[i] = BeanTools.get(bean, columns[i]);
      }
      super.add(obj);
   }

   public void addAll(Collection<? extends T> items) {
      for (T i : items) {
         add(i);
      }
   }

   public TextColumnBuilder<String>[] getColumns(LocalisedItem loci ) {
      TextColumnBuilder<String> res[] = new TextColumnBuilder[columns.length];
      for (int i = 0; i < columns.length; i++) {
         res[i] = col.column(loci.getString(columns[i]), columns[i], type.stringType());
      }
      return res;
   }
}
