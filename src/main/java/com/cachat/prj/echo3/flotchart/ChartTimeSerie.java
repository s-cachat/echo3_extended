package com.cachat.prj.echo3.flotchart;

import java.util.Date;

/**
 *
 * @author scachat
 */
public class ChartTimeSerie extends ChartSerie {

    public ChartTimeSerie(Type type) {
        super(type);
    }

    public void addPoint(Date date, double value) {
        dataPoints.add(new ChartTimePoint(date, value));
    }

    public Iterable<ChartTimePoint> points() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
