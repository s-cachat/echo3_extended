package com.cachat.net;

import jakarta.servlet.http.HttpServletRequest;

/**
 * capacité du navigateur
 *
 * @author scachat
 */
public class BrowserCapability {

    private Browser browser;
    private Integer browserMajorVersion;
    private Integer browserMinorVersion;
    private Integer browserPatchVersion;
    private Platform platform;
    private Integer platformMajorVersion;
    private Integer platformMinorVersion;
    private Integer platformPatchVersion;
    private DeviceType deviceType;
    private boolean html5CanvasSupported;

    public void parseClientHints(HttpServletRequest request) {

        for (String header : new String[]{request.getHeader("http-sec-ch-ua"), request.getHeader("sec-ch-ua")}) {
            Browser bb = Browser.valueFromHeader(header);
            if (bb != null) {
                browser = bb;
                for (String versionHeader : new String[]{request.getHeader("http-sec-ch-ua-full-version-list"), request.getHeader("sec-ch-ua-full-version-list")}) {
                    parseBrowserVersions(versionHeader);
                }
                if (browserMajorVersion == null) {
                    parseBrowserVersions(header);
                }
                break;
            }
        }

        for (String header : new String[]{request.getHeader("http-sec-ch-ua-platform"), request.getHeader("sec-ch-ua-platform"), request.getHeader("platform")}) {
            Platform p = Platform.valueFromHeader(header);
            if (p != null) {
                platform = p;
                for (String versionHeader : new String[]{request.getHeader("http-sec-ch-ua-platform-version"), request.getHeader("sec-ch-ua-platform-version")}) {
                    parsePlatformVersions(versionHeader);
                }
                break;
            }
        }

        for (String header : new String[]{request.getHeader("http-sec-ch-ua-mobile"), request.getHeader("sec-ch-ua-mobile"), request.getHeader("mobile")}) {
            if ("true".equals(header) || "1".equals(header) || "?1".equals(header)) {
                deviceType = DeviceType.PHONE;
                break;
            }
        }
        if (deviceType == null) {
            deviceType = DeviceType.DESKTOP;
        }

        if (browser == null) {
            parseUserAgent(request.getHeader("User-Agent"));
        }

        defineCanvasSupport();
    }

    private void parseBrowserVersions(String header) {
        if (header == null || header.isBlank()) {
            return;
        }

        String[] brands = header.trim().split(", ");
        for (String brand : brands) {
            Browser b = Browser.valueFromHeader(brand);
            if (browser == b) {
                int start = brand.indexOf("\";v=\"") + 5;
                int end = brand.lastIndexOf("\"");
                String[] versions = brand.substring(start, end).split("\\.");
                if (versions.length > 0) {
                    try {
                        browserMajorVersion = Integer.valueOf(versions[0]);

                        if (versions.length > 1) {
                            try {
                                browserMinorVersion = Integer.valueOf(versions[1]);

                                if (versions.length > 2) {
                                    try {
                                        browserPatchVersion = Integer.valueOf(versions[2]);
                                    } catch (NumberFormatException ex) {
                                        //nop
                                    }
                                }
                            } catch (NumberFormatException ex) {
                                //nop
                            }
                        }
                    } catch (NumberFormatException ex) {
                        //nop
                    }
                }
            }
        }
    }

    private void parsePlatformVersions(String header) {
        if (header == null || header.isBlank()) {
            return;
        }

        String[] versions = header.split("\\.");
        if (versions.length > 0) {
            try {
                platformMajorVersion = Integer.valueOf(versions[0]);

                if (versions.length > 1) {
                    try {
                        platformMinorVersion = Integer.valueOf(versions[1]);

                        if (versions.length > 2) {
                            try {
                                platformPatchVersion = Integer.valueOf(versions[2]);
                            } catch (NumberFormatException ex) {
                                //nop
                            }
                        }
                    } catch (NumberFormatException ex) {
                        //nop
                    }
                }
            } catch (NumberFormatException ex) {
                //nop
            }
        }
    }

    private void defineCanvasSupport() {
        if (browser != null && deviceType != DeviceType.ROBOT) {
            switch (browser) {
                case ANDROID_WIR ->
                    html5CanvasSupported = false;
                case SAFARI ->
                    html5CanvasSupported = browserMajorVersion > 5 || (browserMajorVersion == 5 && browserMinorVersion >= 1);
                case CHROME, CHROMIUM ->
                    html5CanvasSupported = browserMajorVersion >= 22;
                case EDGE ->
                    html5CanvasSupported = browserMajorVersion >= 12;
                case FIREFOX ->
                    html5CanvasSupported = browserMajorVersion >= 15;
                case IE ->
                    html5CanvasSupported = browserMajorVersion >= 9;
                case IOS_WIR ->
                    html5CanvasSupported = false;
                case OPERA ->
                    html5CanvasSupported = browserMajorVersion > 12 || (browserMajorVersion == 12 && browserMinorVersion >= 1);
                case OTHER ->
                    html5CanvasSupported = false;
            }
        }
    }

    /**
     * analyse un useragent
     *
     * @param userAgent le user agent
     * @deprecated à supprimer une fois les headers CH deviennent baseline
     */
    @Deprecated
    public void parseUserAgent(String userAgent) {
        String uua = (userAgent == null) ? "" : userAgent.toUpperCase();
        if (uua.startsWith("MOZILLA")) {
            int msie = uua.indexOf("MSIE");
            if (msie > 0) {
                browser = Browser.IE;
                String[] v = uua.substring(msie + 4).split("[\\- \\.;A-Z]");
                //System.err.println(Arrays.asList(v));
                browserMajorVersion = Integer.valueOf(v[1]);
                browserMinorVersion = Integer.valueOf(v[2]);
                if (uua.indexOf("IEMOBILE") > 0) {
                    deviceType = DeviceType.PHONE;
                } else {
                    deviceType = DeviceType.DESKTOP;
                }
            } else {
                int wk = uua.indexOf("APPLEWEBKIT");
                if (wk > 0) {
                    String[] v = uua.substring(wk).split("[ \\.;+/]");
                    if (uua.indexOf("CHROME") > 0) {
                        v = uua.substring(uua.indexOf("CHROME") + 6).split("[_\\- \\.;+/]");
                        deviceType = DeviceType.DESKTOP;
                        browser = Browser.CHROME;
                    } else if (uua.indexOf("MACINTOSH") > 0) {
                        int vp = uua.indexOf("VERSION");
                        if (vp > 0) {
                            v = uua.substring(vp).split("[_ \\.;+/]");
                            deviceType = DeviceType.DESKTOP;
                            browser = Browser.SAFARI;
                        } else {
                            deviceType = DeviceType.ROBOT;
                            browser = null;
                        }
                    } else if (uua.indexOf("IPHONE") > 0) {
                        v = uua.substring(uua.indexOf("CPU OS") + 5).split("[_\\- \\.;+/]");
                        deviceType = DeviceType.PHONE;
                        browser = Browser.SAFARI;
                    } else if (uua.indexOf("IPAD") > 0) {
                        //System.err.println(Arrays.asList( uua.substring(uua.indexOf("CPU OS") + 5).split("[_ \\.;+/]")));
                        v = uua.substring(uua.indexOf("CPU OS") + 5).split("[_ \\.;+/]");

                        deviceType = DeviceType.TABLET;
                        browser = Browser.SAFARI;
                    } else if (uua.indexOf("ANDROID") > 0) {
                        v = uua.substring(uua.indexOf("ANDROID")).split("[_\\- \\.;+/]");
                        browser = Browser.ANDROID;
                        if (uua.indexOf("SCH-I800") > 0) {
                            deviceType = DeviceType.TABLET;
                        } else if (uua.indexOf("GT-P") > 0) {
                            deviceType = DeviceType.TABLET;
                        } else {
                            deviceType = DeviceType.PHONE;
                        }
                    } else if (uua.indexOf("SAFARI") > 0) {
                        int vp = uua.indexOf("VERSION");
                        if (vp > 0) {
                            v = uua.substring(vp).split("[_ \\.;+/]");
                            deviceType = DeviceType.DESKTOP;
                            browser = Browser.SAFARI;
                        } else {
                            deviceType = DeviceType.ROBOT;
                            browser = null;
                        }
                    }
                    if (deviceType != DeviceType.ROBOT) {
                        browserMajorVersion = Integer.parseInt(v[1]);
                        if (!v[2].matches("\\d+")) {
                            if (!v[0].equals("APPLEWEBKIT")) {//Exception
                                browserMinorVersion = 0;
                                //throw new RuntimeException("bad minor\"" + v[2] + "\", type=" + browserType + ", brand=" + browserBrand);
                            } else {
                                browserMinorVersion = 0;
                            }
                        } else {
                            browserMinorVersion = Integer.parseInt(v[2]);
                        }
                    } else {
                        browserMajorVersion = 0;
                        browserMinorVersion = 0;
                    }
                } else {
                    int ff = uua.indexOf("FIREFOX");
                    if (ff > 0) {
                        String[] v = uua.substring(ff + 7).split("[ \\./;,\\-A-Z\\)]");
                        browserMajorVersion = Integer.parseInt(v[1]);
                        browserMinorVersion = Integer.parseInt(v[2]);
                        if (uua.indexOf("MOBILE") > 0) {
                            deviceType = DeviceType.PHONE;
                        } else if (uua.indexOf("TABLET") > 0) {
                            deviceType = DeviceType.TABLET;
                        } else {
                            deviceType = DeviceType.DESKTOP;
                        }
                        browser = Browser.FIREFOX;
                    } else {
                        deviceType = DeviceType.ROBOT;
                        browser = null;
                        browserMajorVersion = 0;
                        browserMajorVersion = 0;
                    }
                }
            }
        } else if (uua.startsWith("OPERA")) {
            if (uua.indexOf("OPERA MOBI") > 0) {
                deviceType = DeviceType.PHONE;
            } else {
                deviceType = DeviceType.DESKTOP;
            }
            browser = Browser.OPERA;
            String[] v = uua.split("[ \\./;,\\-\\)]");
            browserMajorVersion = Integer.parseInt(v[1]);
            browserMinorVersion = Integer.parseInt(v[2]);
        } else if (uua.startsWith("FIREFOX")) {
            if (deviceType == null) {
                if (uua.indexOf("MAEMO") > 0) {
                    deviceType = DeviceType.PHONE;
                } else {
                    deviceType = DeviceType.DESKTOP;
                }
            }
            if (browser == null) {
                browser = Browser.FIREFOX;
            }
            if (browserMajorVersion == 0) {
                browserMajorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
                browserMinorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
            }
        } else if (uua.startsWith("CG05") && uua.indexOf("DARWIN") >= 0) {
            deviceType = DeviceType.PHONE;
            browser = Browser.IOS_WIR;
            browserMajorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            browserMinorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("JAVA")) {
            deviceType = DeviceType.ROBOT;
            browser = Browser.OTHER;
            browserMajorVersion = Integer.parseInt(uua.split("[/\\.]")[1]);
            browserMinorVersion = Integer.parseInt(uua.split("[/\\.]")[2]);
        } else if (uua.startsWith("APACHE-HTTPCLIENT")) {
            deviceType = DeviceType.ROBOT;
            browser = Browser.OTHER;
            browserMajorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            browserMinorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("WGET")) {
            deviceType = DeviceType.ROBOT;
            browser = Browser.OTHER;
            String[] v = uua.split("[ \\./;,\\-\\)]");
            browserMajorVersion = v.length > 1 ? Integer.parseInt(v[1]) : 0;
            browserMinorVersion = v.length > 2 ? Integer.parseInt(v[2]) : 0;
        } else {
            deviceType = DeviceType.ROBOT;
            browser = null;
            browserMajorVersion = 0;
            browserMajorVersion = 0;
        }

    }

    //<editor-fold desc="Browser" defaultstate="collapsed">
    public Browser getBrowser() {
        return browser;
    }

    public Integer getBrowserMajorVersion() {
        return browserMajorVersion;
    }

    public Integer getBrowserMinorVersion() {
        return browserMinorVersion;
    }

    public Integer getBrowserPatchVersion() {
        return browserPatchVersion;
    }

    public String getBrowserFullVersion() {
        if (browserMajorVersion != null) {
            if (browserMinorVersion != null) {
                if (browserPatchVersion != null) {
                    return String.format("%d.%d.%d", browserMajorVersion, browserMinorVersion, browserPatchVersion);
                } else {
                    return String.format("%d.%d", browserMajorVersion, browserMinorVersion);
                }
            } else {
                return String.valueOf(browserMajorVersion);
            }
        }
        return "?";
    }

    public static enum Browser {

        IOS_WIR(null),
        ANDROID_WIR(null),
        CHROME("Google Chrome"),
        CHROMIUM("Chromium"),
        EDGE("Microsoft Edge"),
        FIREFOX(null),
        SAFARI(null),
        OPERA("Opera"),
        IE(null),
        OTHER(null),
        ANDROID(null);

        /**
         * La valeur du header Sec-CH-UA
         */
        private final String header;

        /**
         * Constructeur
         *
         * @param header La valeur du header Sec-CH-UA
         */
        private Browser(String header) {
            this.header = header;
        }

        /**
         * Donne le Browser pour une valeur du header Sec-CH-UA.
         *
         * @param header La valeur du header Sec-CH-UA
         * @return le Browser, ou null si aucun correspond
         */
        public static Browser valueFromHeader(String header) {
            if (header == null || header.isBlank()) {
                return null;
            }
            for (Browser bb : values()) {
                if (bb.header == null) {
                    continue;
                }

                // NOTE : on teste Chromium après car plusieurs navigateurs sont
                // basés là-dessous; si le header contient "Chromium" sans 
                // d'autres valeurs, c'est un Chromium générique.
                if (bb == CHROMIUM) {
                    continue;
                }

                if (header.contains(bb.header)) {
                    return bb;
                }
            }
            if (header.contains(CHROMIUM.header)) {
                return CHROMIUM;
            }
            return null;
        }
    }

    //</editor-fold>
    //<editor-fold desc="Platform" defaultstate="collapsed">
    public Platform getPlatform() {
        return platform;
    }

    public Integer getPlatformMajorVersion() {
        return platformMajorVersion;
    }

    public Integer getPlatformMinorVersion() {
        return platformMinorVersion;
    }

    public Integer getPlatformPatchVersion() {
        return platformPatchVersion;
    }

    public String getPlatformFullVersion() {
        if (platformMajorVersion != null) {
            if (platformMinorVersion != null) {
                if (platformPatchVersion != null) {
                    return String.format("%d.%d.%d", platformMajorVersion, platformMinorVersion, platformPatchVersion);
                } else {
                    return String.format("%d.%d", platformMajorVersion, platformMinorVersion);
                }
            } else {
                return String.valueOf(platformMajorVersion);
            }
        }
        return "?";
    }

    public static enum Platform {
        ANDROID("Android"),
        CHROME("Chrome OS"),
        CHROMIUM("Chromium OS"),
        IOS("iOS"),
        LINUX("Linux"),
        MACOS("macOS"),
        WINDOW("Windows"),
        UNKNOWN("Unknown");

        /**
         * La valeur du header Sec-CH-UA-Platform
         */
        private final String header;

        /**
         * Constructeur
         *
         * @param header La valeur du header Sec-CH-UA-Platform
         */
        private Platform(String header) {
            this.header = header;
        }

        /**
         * Donne le Platform qui correspond à la valeur d'un header
         * Sec-CH-UA-Platform.
         *
         * @param header La valeur du header Sec-CH-UA-Platform
         * @return le Platform, ou null si aucun correspond
         */
        public static Platform valueFromHeader(String header) {
            if (header == null || header.isBlank()) {
                return null;
            }
            for (Platform pl : values()) {
                if (header.contains(pl.header)) {
                    return pl;
                }
            }
            return null;
        }
    }

    //</editor-fold>
    //<editor-fold desc="Device type" defaultstate="collapsed">
    public DeviceType getDeviceType() {
        return deviceType;
    }

    public static enum DeviceType {

        ROBOT,
        DESKTOP,
        TABLET,
        PHONE
    }
    //</editor-fold>

    public boolean isHtml5CanvasSupported() {
        return html5CanvasSupported;
    }

    public boolean isIE8orLess() {
        return browser == Browser.IE && getBrowserMajorVersion() <= 8;
    }

    @Override
    public String toString() {
        return "BrowserCapability{" + "browser=" + browser + ", browserVersion=" + getBrowserFullVersion() + ", platform=" + platform + ", platformVersion=" + getPlatformFullVersion() + ", deviceType=" + deviceType + ", html5CanvasSupported=" + html5CanvasSupported + '}';
    }

}
