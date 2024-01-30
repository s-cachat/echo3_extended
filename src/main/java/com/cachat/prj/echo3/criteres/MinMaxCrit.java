package com.cachat.prj.echo3.criteres;

import java.util.ArrayList;
import java.util.List;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Label;
import nextapp.echo.app.Row;
import nextapp.echo.app.TextField;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;

/**
 * un critere de type mini maxi
 *
 * @author scachat
 */
public abstract class MinMaxCrit extends Crit implements ActionListener {

    /**
     * Regex pour n'importe de quel nombre
     */
    protected static final String REGEX = "[+-]?\\d+(.\\d+)*";
    /**
     * la date de debut
     */
    protected final TextField tfMin;
    /**
     * la date de fin
     */
    protected final TextField tfMax;

    /**
     * constructeur
     *
     * @param cont le conteneur (la liste)
     * @param prop le nom de la propriete critere
     */
    public MinMaxCrit(CritContainer cont, String prop) {
        super(cont, prop);
        critf.add(newLabel(cont.getString(prop), cont.getString(prop + ".tt")));
        Row row = new Row();
        tfMin = new TextField();
        tfMin.setStyleName("Grid");
        tfMin.setWidth(new Extent(75, Extent.PX));
        tfMin.setHeight(new Extent(20, Extent.PX));
        tfMin.addActionListener(this);
        row.add(tfMin);
        row.add(new Label("=>"));
        tfMax = new TextField();
        tfMax.setStyleName("Grid");
        tfMax.setWidth(new Extent(75, Extent.PX));
        tfMax.setHeight(new Extent(20, Extent.PX));
        tfMax.addActionListener(this);
        row.add(tfMax);
        critf.add(row);
        cont.addCrit(this);
        addActionListener(cont);
        cont.extendCritAreaHeight(CritContainer.CRIT_HEIGHT);
    }

    /**
     * traite l'action
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        ActionEvent ae = new ActionEvent(this, "DATE");
        listeners.stream().forEach((a) -> a.actionPerformed(ae));
    }
    /**
     * les actionListener
     */
    private final List<ActionListener> listeners = new ArrayList<>();

    /**
     * ajoute un actionListener
     *
     * @param a le listener
     */
    public final void addActionListener(ActionListener a) {
        listeners.add(a);
    }

    public static class Float extends MinMaxCrit {

        public Float(CritContainer cont, String prop) {
            super(cont, prop);
        }

        /**
         * donne le minimum
         *
         * @return le minimum
         */
        public float getMin() {
            String t = tfMin.getText();
            return (t == null || !t.matches(REGEX)) ? java.lang.Float.NaN : java.lang.Float.parseFloat(t);
        }

        /**
         * donne le maximum
         *
         * @return le maximum
         */
        public float getMax() {
            String t = tfMax.getText();
            return (t == null || !t.matches(REGEX)) ? java.lang.Float.NaN : java.lang.Float.parseFloat(t);
        }

        public void setMin(float d) {
            tfMin.setText(java.lang.Float.isNaN(d) ? "" : String.valueOf(d));
        }

        public void setMax(float d) {
            tfMax.setText(java.lang.Float.isNaN(d) ? "" : String.valueOf(d));
        }

        /**
         * met a jour le where
         *
         * @return le fragment de chaine where
         * @param arg la liste des arguments a completer
         */
        @Override
        public String updateWhere(List<Object> arg) {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    float max = java.lang.Float.parseFloat(max_);
                    arg.add(max);
                    return String.format("%1$s<?", prop);
                }
            } else {
                float min = java.lang.Float.parseFloat(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    arg.add(min);
                    return String.format("%1$s>=?", prop);
                } else {
                    float max = java.lang.Float.parseFloat(max_);
                    arg.add(max);
                    arg.add(min);
                    return String.format("%1$s<? and %2$s>=?", prop, prop);
                }
            }
        }

        @Override
        public String getSummary() {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            String p = cont.getString(prop);
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    float max = java.lang.Float.parseFloat(max_);
                    return String.format("%1$s < %2$f", p, max);
                }
            } else {
                float min = java.lang.Float.parseFloat(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return String.format("%1$s >= %2$f", p, min);
                } else {
                    float max = java.lang.Float.parseFloat(max_);
                    return String.format("%2$f < %1$s >= %3$f", p, max, min);
                }
            }
        }
    }

    public static class Double extends MinMaxCrit {

        public Double(CritContainer cont, String prop) {
            super(cont, prop);
        }

        /**
         * donne le minimum
         *
         * @return le minimum
         */
        public double getMin() {
            String t = tfMin.getText();
            return (t == null || !t.matches(REGEX)) ? java.lang.Double.NaN : java.lang.Double.parseDouble(t);
        }

        /**
         * donne le maximum
         *
         * @return le maximum
         */
        public double getMax() {
            String t = tfMax.getText();
            return (t == null || !t.matches(REGEX)) ? java.lang.Double.NaN : java.lang.Double.parseDouble(t);
        }

        public void setMin(double d) {
            tfMin.setText(java.lang.Double.isNaN(d) ? "" : String.valueOf(d));
        }

        public void setMax(double d) {
            tfMax.setText(java.lang.Double.isNaN(d) ? "" : String.valueOf(d));
        }

        /**
         * met a jour le where
         *
         * @return le fragment de chaine where
         * @param arg la liste des arguments a completer
         */
        @Override
        public String updateWhere(List<Object> arg) {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    double max = java.lang.Double.parseDouble(max_);
                    arg.add(max);
                    return String.format("%1$s<?", prop);
                }
            } else {
                double min = java.lang.Double.parseDouble(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    arg.add(min);
                    return String.format("%1$s>?", prop);
                } else {
                    double max = java.lang.Double.parseDouble(max_);
                    arg.add(max);
                    arg.add(min);
                    return String.format("%1$s<? and %2$s>?", prop, prop);
                }
            }
        }

        @Override
        public String getSummary() {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            String p = cont.getString(prop);
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    double max = java.lang.Double.parseDouble(max_);
                    return String.format("%1$s < %2$f", p, max);
                }
            } else {
                double min = java.lang.Double.parseDouble(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return String.format("%1$s >= %2$f", p, min);
                } else {
                    double max = java.lang.Double.parseDouble(max_);
                    return String.format("%2$f < %1$s >= %3$f", p, max, min);
                }
            }
        }
    }

    public static class Integer extends MinMaxCrit {

        public Integer(CritContainer cont, String prop) {
            super(cont, prop);
        }

        /**
         * donne le minimum
         *
         * @return le minimum
         */
        public int getMin() {
            String t = tfMin.getText();
            return (t == null || !t.matches(REGEX)) ? 0 : java.lang.Integer.parseInt(t);
        }

        /**
         * donne le maximum
         *
         * @return le maximum
         */
        public int getMax() {
            String t = tfMax.getText();
            return (t == null || !t.matches(REGEX)) ? 0 : java.lang.Integer.parseInt(t);
        }

        public void setMin(int d) {
            tfMin.setText(String.valueOf(d));
        }

        public void setMax(int d) {
            tfMax.setText(String.valueOf(d));
        }

        /**
         * met a jour le where
         *
         * @return le fragment de chaine where
         * @param arg la liste des arguments a completer
         */
        @Override
        public String updateWhere(List<Object> arg) {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    int max = java.lang.Integer.parseInt(max_);
                    arg.add(max);
                    return String.format("%1$s<?", prop);
                }
            } else {
                int min = java.lang.Integer.parseInt(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    arg.add(min);
                    return String.format("%1$s>?", prop);
                } else {
                    int max = java.lang.Integer.parseInt(max_);
                    arg.add(max);
                    arg.add(min);
                    return String.format("%1$s<? and %2$s>?", prop, prop);
                }
            }
        }

        @Override
        public String getSummary() {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            String p = cont.getString(prop);
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    int max = java.lang.Integer.parseInt(max_);
                    return String.format("%1$s < %2$d", p, max);
                }
            } else {
                int min = java.lang.Integer.parseInt(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return String.format("%1$s >= %2$d", p, min);
                } else {
                    int max = java.lang.Integer.parseInt(max_);
                    return String.format("%2$d < %1$s >= %3$d", p, max, min);
                }
            }
        }
    }

    public static class Long extends MinMaxCrit {

        public Long(CritContainer cont, String prop) {
            super(cont, prop);
        }

        /**
         * donne le minimum
         *
         * @return le minimum
         */
        public long getMin() {
            String t = tfMin.getText();
            return (t == null || !t.matches("\\d+")) ? 0 : java.lang.Long.parseLong(t);
        }

        /**
         * donne le maximum
         *
         * @return le maximum
         */
        public long getMax() {
            String t = tfMax.getText();
            return (t == null || !t.matches("\\d+")) ? 0 : java.lang.Long.parseLong(t);
        }

        public void setMin(long d) {
            tfMin.setText(String.valueOf(d));
        }

        public void setMax(long d) {
            tfMax.setText(String.valueOf(d));
        }

        /**
         * met a jour le where
         *
         * @return le fragment de chaine where
         * @param arg la liste des arguments a completer
         */
        @Override
        public String updateWhere(List<Object> arg) {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    long max = java.lang.Long.parseLong(max_);
                    arg.add(max);
                    return String.format("%1$s<?", prop);
                }
            } else {
                long min = java.lang.Long.parseLong(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    arg.add(min);
                    return String.format("%1$s>?", prop);
                } else {
                    long max = java.lang.Long.parseLong(max_);
                    arg.add(max);
                    arg.add(min);
                    return String.format("%1$s<? and %2$s>?", prop, prop);
                }
            }
        }

        @Override
        public String getSummary() {
            String min_ = tfMin.getText();
            String max_ = tfMax.getText();
            String p = cont.getString(prop);
            if ((min_ == null || !min_.matches(REGEX))) {//pas de min
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return null;
                } else {
                    long max = java.lang.Long.parseLong(max_);
                    return String.format("%1$s < %2$d", p, max);
                }
            } else {
                long min = java.lang.Long.parseLong(min_);
                if ((max_ == null || !max_.matches(REGEX))) {//pas de max
                    return String.format("%1$s >= %2$d", p, min);
                } else {
                    long max = java.lang.Long.parseLong(max_);
                    return String.format("%2$d < %1$s >= %3$d", p, max, min);
                }
            }
        }
    }
}
