package com.cachat.prj.echo3.components;

import java.util.Date;
import nextapp.echo.app.Component;
import nextapp.echo.app.update.ClientUpdateManager;
import nextapp.echo.app.util.Context;
import nextapp.echo.webcontainer.AbstractComponentSynchronizePeer;
import nextapp.echo.webcontainer.ServerMessage;
import nextapp.echo.webcontainer.Service;
import nextapp.echo.webcontainer.WebContainerServlet;
import nextapp.echo.webcontainer.service.JavaScriptService;

/**
 * Peer pour un composant DateSelect2
 *
 * @author user1
 */
public class DateSelect2Peer extends AbstractComponentSynchronizePeer {

    private static final Service MOMENT_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.moment.min", "com/cachat/prj/echo3/components/js/moment.min.js");
    private static final Service DATETIMERANGE_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.datetimerange", "com/cachat/prj/echo3/components/js/datetimerange.js");
    private static final Service JS_SERVICE = JavaScriptService.forResource("com.cachat.prj.echo3.components.DateSelect2", "com/cachat/prj/echo3/components/js/DateSelect2.js");

    static {
        WebContainerServlet.getServiceRegistry().add(JS_SERVICE);
        WebContainerServlet.getServiceRegistry().add(DATETIMERANGE_SERVICE);
        WebContainerServlet.getServiceRegistry().add(MOMENT_SERVICE);
    }

    public DateSelect2Peer() {
        for (String prop : DateSelect2.getOutputProperties()) {
            addOutputProperty(prop);
        }

        addEvent(new EventPeer(DateSelect2.ON_APPLY_ACTION, DateSelect2.APPLY_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasApplyListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_SHOW_ACTION, DateSelect2.SHOW_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasShowListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_HIDE_ACTION, DateSelect2.HIDE_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasHideListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_SHOW_CALENDAR_ACTION, DateSelect2.SHOW_CALENDAR_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasShowCalendarListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_HIDE_CALENDAR_ACTION, DateSelect2.HIDE_CALENDAR_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasHideCalendarListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_CANCEL_ACTION, DateSelect2.CANCEL_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasCancelListeners();
            }
        });
        addEvent(new EventPeer(DateSelect2.ON_CLEAR_ACTION, DateSelect2.CLEAR_LISTENERS_CHANGED_PROPERTY, String.class) {
            @Override
            public boolean hasListeners(Context context, Component c) {
                return ((DateSelect2) c).hasClearListeners();
            }
        });
    }

    @Override
    public String getClientComponentType(boolean shortType) {
        return "DateSelect2";
    }

    @Override
    public Class getComponentClass() {
        return DateSelect2.class;
    }

    @Override
    public Class getInputPropertyClass(String propertyName) {
        return switch (propertyName) {
            case DateSelect2.PROPERTY_START_DATE_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_END_DATE_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_MIN_DATE_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_MAX_DATE_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_MIN_YEAR_VALUE ->
                Integer.class;
            case DateSelect2.PROPERTY_MAX_YEAR_VALUE ->
                Integer.class;
            case DateSelect2.PROPERTY_DATE_LIMIT_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_AUTO_APPLY_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_SINGLE_DATE_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_SHOW_DROPDOWNS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_SHOW_WEEK_NUMBERS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_SHOW_ISO_WEEK_NUMBERS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_SHOW_CUSTOM_RANGE_LABEL_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_TIME_PICKER_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_TIME_PICKER_24H_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_TIME_PICKER_INCREMENT_VALUE ->
                Integer.class;
            case DateSelect2.PROPERTY_TIME_PICKER_SECONDS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_LINKED_CALENDARS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_AUTO_UPDATE_INPUT_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_ALWAYS_SHOW_CALENDARS_VALUE ->
                Boolean.class;
            case DateSelect2.PROPERTY_RANGES_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_OPENS_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_DROPS_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_BUTTON_CLASSES_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_APPLY_BUTTON_CLASSES_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_CANCEL_BUTTON_CLASSES_VALUE ->
                String.class;
            case DateSelect2.PROPERTY_LOCALE_VALUE ->
                String.class;
            default ->
                null;
        };
    }

    @Override
    public void storeInputProperty(Context context, Component component, String propertyName, int propertyIndex, Object newValue) {
        ClientUpdateManager clientUpdateManager = (ClientUpdateManager) context.get(ClientUpdateManager.class);
        clientUpdateManager.setComponentProperty(component, propertyName, newValue);
    }

    @Override
    public void init(Context context, Component component) {
        super.init(context, component);
        // Obtain outgoing 'ServerMessage' for initial render.
        ServerMessage serverMessage = (ServerMessage) context.get(ServerMessage.class);
        // Add ContainerEx JavaScript library to client.
        serverMessage.addLibrary(JS_SERVICE.getId());
        serverMessage.addLibrary(DATETIMERANGE_SERVICE.getId());
        serverMessage.addLibrary(MOMENT_SERVICE.getId());
    }
}
