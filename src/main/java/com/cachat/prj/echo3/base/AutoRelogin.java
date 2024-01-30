package com.cachat.prj.echo3.base;

import com.cachat.util.NumberUtil;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe permettant un autorelogin en cas de rupture de connexion avec le
 * serveur
 *
 * @author scachat
 */
public class AutoRelogin {

    private static final Random random = new Random();
    private static final Logger logger = Logger.getLogger(AutoRelogin.class.getName());

    public static String getAutoRelogin(String user, String ip) {
        try {
            String a = String.format("%s|%s|%d|%04x", user, ip, System.currentTimeMillis(), (random.nextInt() & 0xffff));
            byte[] b = MessageDigest.getInstance("MD5").digest(a.getBytes());
            String c = String.format("%s%s", a, NumberUtil.bytesToHex(b));
            return c;
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Verifie la chaine d'auto reconnexion
     *
     * @param s la chaine
     * @param ip l'adresse ip du requerant
     * @param maxTime l'age maxi que doit avoir la chaine
     * @return le user name ou null si la chaine est invalide
     */
    public static String checkAutoRelogin(String s, String ip, long maxTime) {
        try {
            if (s == null) {
                return null;
            }
            String items[] = s.split("\\|");
            if (items.length <= 1) {
                return null;
            }
            if (items.length != 4) {
                logger.log(Level.FINEST, "autoRelogin : reject car nb de param invalide {0} :{1}", new Object[]{items.length, s});
                return null;
            }
            if (ip != null && !ip.equals(items[1])) {
                if (!("127.0.0.1".equals(ip)) && !("0:0:0:0:0:0:0:1".equals(ip))) {
                logger.log(Level.FINEST, "autoRelogin : reject car ip differente \"{0}\" et \"{1}\"", new Object[]{items[1], ip});
                }
                return null;
            }
            if (!items[2].matches("\\d+")) {
                logger.log(Level.FINEST, "autoRelogin : reject car ts invalide \"{0}", s);
                return null;
            }
            long ts = Long.parseLong(items[2]);
            if (Math.abs(System.currentTimeMillis() - ts) > maxTime) {
                //logger.log(Level.WARNING, "autoRelogin : reject car cle trop vieille \"{0}", s);
                return null;
            }
            if (items[3].length() < 5) {
                logger.log(Level.FINEST, "autoRelogin : reject car longueur de cle invalide \"{0}", s);
                return null;
            }
            String salt = items[3].substring(0, 4);
            String a = String.format("%s|%s|%d|%s", items[0], items[1], ts, salt);
            byte[] b = MessageDigest.getInstance("MD5").digest(a.getBytes());
            String c = NumberUtil.bytesToHex(b);
            if (c.equals(items[3].substring(4))) {
                logger.log(Level.FINEST, "autoRelogin : ok pour \"{0}", items[0]);
                return items[0];
            } else {
                logger.log(Level.WARNING, "autoRelogin : reject car cle invalide \"{0}", s);
                return null;
            }
        } catch (Throwable ex) {
            logger.log(Level.WARNING, "autoRelogin : reject car erreur \"{0}", s);
            logger.log(Level.SEVERE, null, ex);
            return null;
        }

    }

    /**
     * Revalide la chaine d'auto reconnexion
     *
     * @param s la chaine
     * @param ip l'adresse ip du requerant
     * @param maxTime l'age maxi que doit avoir la chaine
     * @return la nouvelle chaine ou null si l'originale était invalide
     */
    public static String updateAutoRelogin(String s, String ip, long maxTime) {
        String user = checkAutoRelogin(s, ip, maxTime);
        if (user == null) {
            return null;
        } else {
            return getAutoRelogin(user, ip);
        }
    }
}
