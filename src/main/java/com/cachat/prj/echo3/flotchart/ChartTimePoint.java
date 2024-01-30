package com.cachat.prj.echo3.flotchart;

import java.util.Date;

/**
 *
 * @author scachat
 */
public class ChartTimePoint extends ChartPoint {

    private Date date;
    private double value;

    public ChartTimePoint(Date date, double value) {
        super(date.getTime(), value);
    }

    public Date getDate() {
        return x == null ? null : new Date(Long.valueOf(x));
    }

    public void setDate(Date date) {
        x = String.valueOf(date.getTime());
    }
}
