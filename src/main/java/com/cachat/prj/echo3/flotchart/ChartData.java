package com.cachat.prj.echo3.flotchart;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.ArrayList;
import java.util.List;

/**
 * les données a transmettres pour mise à jour. ne contient que les données à
 * tracer.
 *
 * @author scachat
 */
@JsonRootName(value = "flotData")
public class ChartData {

    private final List<ChartSerie> series = new ArrayList<>();

    public ChartData(ChartModel model) {
        series.addAll(model.getSeries());
    }

    public ChartData() {
    }

    public List<ChartSerie> getSeries() {
        return series;
    }

    public void process() {
        int i = 0;
        for (ChartSerie cs : series) {
            cs.process();
            if (cs.getType() == ChartSerie.Type.BAR && cs.getSpec().getOrder() == null) {
                cs.getSpec().setOrder(i++);
            }
        }
    }
}
