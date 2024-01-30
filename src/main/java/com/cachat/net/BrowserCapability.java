package com.cachat.net;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * capacité du navigateur
 *
 * @author scachat
 */
public class BrowserCapability {

    private boolean html5CanvasSupported;
    private BrowserType browserType;
    private int minorVersion;
    private int majorVersion;
    private BrowserBrand browserBrand;
    private String remoteIp;

    /**
     * analyse un useragent
     *
     * @param userAgent le user agent
     * @param remoteIp l'adresse ip du client
     */
    public void parseUserAgent(String userAgent, String remoteIp) {
        parseUserAgent(userAgent);
        this.remoteIp = remoteIp;
    }

    /**
     * analyse un useragent
     *
     * @param userAgent le user agent
     */
    public void parseUserAgent(String userAgent) {
        String uua = (userAgent == null) ? "" : userAgent.toUpperCase();
        if (uua.startsWith("MOZILLA")) {
            int msie = uua.indexOf("MSIE");
            if (msie > 0) {
                browserBrand = BrowserBrand.IE;
                String[] v = uua.substring(msie + 4).split("[\\- \\.;A-Z]");
                //System.err.println(Arrays.asList(v));
                majorVersion = Integer.parseInt(v[1]);
                minorVersion = Integer.parseInt(v[2]);
                if (uua.indexOf("IEMOBILE") > 0) {
                    browserType = BrowserType.PHONE;
                } else {
                    browserType = BrowserType.DESKTOP;
                }
            } else {
                int wk = uua.indexOf("APPLEWEBKIT");
                if (wk > 0) {
                    String[] v = uua.substring(wk).split("[ \\.;+/]");
                    if (uua.indexOf("CHROME") > 0) {
                        v = uua.substring(uua.indexOf("CHROME") + 6).split("[_\\- \\.;+/]");
                        browserType = BrowserType.DESKTOP;
                        browserBrand = BrowserBrand.CHROME;
                    } else if (uua.indexOf("MACINTOSH") > 0) {
                        int vp = uua.indexOf("VERSION");
                        if (vp > 0) {
                            v = uua.substring(vp).split("[_ \\.;+/]");
                            browserType = BrowserType.DESKTOP;
                            browserBrand = BrowserBrand.SAFARI;
                        } else {
                            browserType = BrowserType.ROBOT;
                            browserBrand = null;
                        }
                    } else if (uua.indexOf("IPHONE") > 0) {
                        v = uua.substring(uua.indexOf("CPU OS") + 5).split("[_\\- \\.;+/]");
                        browserType = BrowserType.PHONE;
                        browserBrand = BrowserBrand.SAFARI;
                    } else if (uua.indexOf("IPAD") > 0) {
                        //System.err.println(Arrays.asList( uua.substring(uua.indexOf("CPU OS") + 5).split("[_ \\.;+/]")));
                        v = uua.substring(uua.indexOf("CPU OS") + 5).split("[_ \\.;+/]");

                        browserType = BrowserType.TABLET;
                        browserBrand = BrowserBrand.SAFARI;
                    } else if (uua.indexOf("ANDROID") > 0) {
                        v = uua.substring(uua.indexOf("ANDROID")).split("[_\\- \\.;+/]");
                        browserBrand = BrowserBrand.ANDROID;
                        if (uua.indexOf("SCH-I800") > 0) {
                            browserType = BrowserType.TABLET;
                        } else if (uua.indexOf("GT-P") > 0) {
                            browserType = BrowserType.TABLET;
                        } else {
                            browserType = BrowserType.PHONE;
                        }
                    } else if (uua.indexOf("DOLFIN") > 0) {
                        v = uua.substring(uua.indexOf("DOLFIN")).split("[_\\- \\.;+/]");
                        browserType = BrowserType.PHONE;
                        browserBrand = BrowserBrand.DOLFIN;
                    } else if (uua.indexOf("BLACKBERRY") > 0) {
                        browserType = BrowserType.PHONE;
                        browserBrand = BrowserBrand.BLACKBERRY;
                    } else if (uua.indexOf("SAFARI") > 0) {
                        int vp = uua.indexOf("VERSION");
                        if (vp > 0) {
                            v = uua.substring(vp).split("[_ \\.;+/]");
                            browserType = BrowserType.DESKTOP;
                            browserBrand = BrowserBrand.SAFARI;
                        } else {
                            browserType = BrowserType.ROBOT;
                            browserBrand = null;
                        }
                    }
                    if (browserType != BrowserType.ROBOT) {
                        majorVersion = Integer.parseInt(v[1]);
                        if (!v[2].matches("\\d+")) {
                            if (!v[0].equals("APPLEWEBKIT")) {//Exception
                                throw new RuntimeException("bad minor\"" + v[2] + "\", type=" + browserType + ", brand=" + browserBrand);
                            } else {
                                minorVersion = 0;
                            }
                        } else {
                            minorVersion = Integer.parseInt(v[2]);
                        }
                    } else {
                        majorVersion = 0;
                        minorVersion = 0;
                    }
                } else {
                    int ff = uua.indexOf("FIREFOX");
                    if (ff > 0) {
                        String[] v = uua.substring(ff + 7).split("[ \\./;,\\-A-Z\\)]");
                        majorVersion = Integer.parseInt(v[1]);
                        minorVersion = Integer.parseInt(v[2]);
                        browserType = BrowserType.DESKTOP;
                        browserBrand = BrowserBrand.FIREFOX;
                    } else {
                        browserType = BrowserType.ROBOT;
                        browserBrand = null;
                        majorVersion = 0;
                        majorVersion = 0;
                    }
                }
            }
        } else if (uua.startsWith("OPERA")) {
            if (uua.indexOf("OPERA MOBI") > 0) {
                browserType = BrowserType.PHONE;
            } else {
                browserType = BrowserType.DESKTOP;
            }
            browserBrand = BrowserBrand.OPERA;
            String[] v = uua.split("[ \\./;,\\-\\)]");
            majorVersion = Integer.parseInt(v[1]);
            minorVersion = Integer.parseInt(v[2]);
        } else if (uua.startsWith("FIREFOX")) {
            if (uua.indexOf("MAEMO") > 0) {
                browserType = BrowserType.PHONE;
            } else {
                browserType = BrowserType.DESKTOP;
            }
            browserBrand = BrowserBrand.FIREFOX;
            majorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("BLACKBERRY")) {
            browserType = BrowserType.PHONE;
            browserBrand = BrowserBrand.BLACKBERRY;
            majorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("CG05") && uua.indexOf("DARWIN") >= 0) {
            browserType = BrowserType.PHONE;
            browserBrand = BrowserBrand.IOS_WIR;
            majorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("BLACKBERRY")) {
            browserType = BrowserType.PHONE;
            browserBrand = BrowserBrand.BLACKBERRY;
            majorVersion = Integer.parseInt(uua.split("[/\\.]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\.]")[2]);
        } else if (uua.startsWith("JAVA")) {
            browserType = BrowserType.ROBOT;
            browserBrand = BrowserBrand.OTHER;
            majorVersion = Integer.parseInt(uua.split("[/\\.]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\.]")[2]);
        } else if (uua.startsWith("APACHE-HTTPCLIENT")) {
            browserType = BrowserType.ROBOT;
            browserBrand = BrowserBrand.OTHER;
            majorVersion = Integer.parseInt(uua.split("[/\\. ]")[1]);
            minorVersion = Integer.parseInt(uua.split("[/\\. ]")[2]);
        } else if (uua.startsWith("WGET")) {
            browserType = BrowserType.ROBOT;
            browserBrand = BrowserBrand.OTHER;
            String[] v = uua.split("[ \\./;,\\-\\)]");
            majorVersion = v.length > 1 ? Integer.parseInt(v[1]) : 0;
            minorVersion = v.length > 2 ? Integer.parseInt(v[2]) : 0;
        } else {
            browserType = BrowserType.ROBOT;
            browserBrand = null;
            majorVersion = 0;
            majorVersion = 0;
        }
        if (browserBrand != null && browserType != BrowserType.ROBOT) {
            switch (browserBrand) {
                case ANDROID_WIR:
                    html5CanvasSupported = false;
                    break;
                case SAFARI:
                    html5CanvasSupported = majorVersion > 5 || (majorVersion == 5 && minorVersion >= 1);
                    break;
                case BLACKBERRY:
                    html5CanvasSupported = majorVersion >= 7;
                    break;
                case CHROME:
                    html5CanvasSupported = majorVersion >= 22;
                    break;
                case FIREFOX:
                    html5CanvasSupported = majorVersion >= 15;
                    break;
                case IE:
                    html5CanvasSupported = majorVersion >= 9;
                    break;
                case IOS_WIR:
                    html5CanvasSupported = false;
                    break;
                case OPERA:
                    html5CanvasSupported = majorVersion > 12 || (majorVersion == 12 && minorVersion >= 1);
                    break;
                case DOLFIN:
                    html5CanvasSupported = false;
                    break;
                case OTHER:
                    html5CanvasSupported = false;
                    break;
            }
        }
    }

    public boolean isHtml5CanvasSupported() {
        return html5CanvasSupported;
    }

    public BrowserBrand getBrowserBrand() {
        return browserBrand;
    }

    public boolean isIE8orLess() {

        if (browserBrand == BrowserBrand.IE && getMajorVersion() <= 8) {
            if (remoteIp == null) {
                Logger.getLogger(getClass().getName()).severe("Poor guy, he got ie, and it is 8.0 or older !");
            } else {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Poor guy ({0}), he got ie, and it is 8.0 or older !", remoteIp);
            }
            return true;
        }
        return false;

    }

    public static enum BrowserBrand {

        IOS_WIR,
        ANDROID_WIR,
        CHROME,
        FIREFOX,
        SAFARI,
        OPERA,
        IE,
        OTHER,
        BLACKBERRY, ANDROID, DOLFIN
    }

    public BrowserType getBrowserType() {
        return browserType;
    }

    public static enum BrowserType {

        ROBOT,
        DESKTOP,
        TABLET,
        PHONE
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    @Override
    public String toString() {
        return "BrowserCapability{" + "html5CanvasSupported=" + html5CanvasSupported + ", browserType=" + browserType + ", minorVersion=" + minorVersion + ", majorVersion=" + majorVersion + ", browserBrand=" + browserBrand + '}';
    }
}
