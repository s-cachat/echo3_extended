package com.cachat.prj.echo3.outils;

import com.cachat.prj.echo3.base.BaseApp;
import com.cachat.prj.echo3.base.BasicWindow;
import com.cachat.service.ServiceRegistry;
import com.cachat.prj.echo3.ng.ButtonEx;
import com.cachat.prj.echo3.ng.CheckBoxEx;
import com.cachat.prj.echo3.ng.LabelEx;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import nextapp.echo.app.Color;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.layout.GridLayoutData;

/**
 *
 * @author scachat
 */
public class ServicePane extends BasicWindow {

    List<Status> statusList = new ArrayList<Status>();

    public ServicePane(BaseApp app) {
        super(app, "Services", "outils", new Extent(800), new Extent(600));
        Grid g = new Grid(5);
        add(g);
        ButtonEx refresh = new ButtonEx(getString("update"));
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        g.add(refresh);
        GridLayoutData gl = new GridLayoutData();
        gl.setColumnSpan(5);
        refresh.setLayoutData(gl);

        Status st;
        for (String sn : ServiceRegistry.getInstance().serviceNames()) {
            g.add(new LabelEx(sn));
            g.add(new StartButton(sn));
            g.add(new StopButton(sn));
            statusList.add(st = new Status(sn));
            g.add(st);
            g.add(new AutoStartBox(sn));
        }
    }

    public void update() {
        for (Status s : statusList) {
            s.update();
        }
    }

    public class StartButton extends ButtonEx implements ActionListener {

        private final String s;

        public StartButton(String s) {
            super(getString("start"));
            this.s = s;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                ServiceRegistry.getInstance().start(s);
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Arrêt de " + s, t);
            }
        }
    }

    public class StopButton extends ButtonEx implements ActionListener {

        private final String s;

        public StopButton(String s) {
            super(getString("stop"));
            this.s = s;
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ServiceRegistry.getInstance().stop(s);
        }
    }

    public class Status extends LabelEx {

        private final String s;

        public Status(String s) {
            this.s = s;
            update();
        }

        public final void update() {
            if (ServiceRegistry.getInstance().isAlive(s)) {
                setBackground(Color.GREEN);
                setText(getString("running"));
            } else {
                setBackground(Color.RED);
                setText(getString("stopped"));
            }
        }
    }

    class AutoStartBox extends CheckBoxEx implements ActionListener {

        private final String s;

        public AutoStartBox(String s) {
            super(getString("autoStart"));
            this.s = s;
            setSelected(ServiceRegistry.getInstance().isAutoStart(s));
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ServiceRegistry.getInstance().setAutoStart(s, isSelected());
        }
    }
}
