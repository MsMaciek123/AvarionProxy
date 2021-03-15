package net.avarioncode.proxy.utils;

import net.avarioncode.proxy.AvarionProxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;

public class ProxyUtils {

    public static final Random random = new Random();

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String randomString(final int len) {
        final StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(random.nextInt(AB.length())));
        return sb.toString();
    }

    public static Proxy getProxyByName(final String pString) {
        if (pString.equalsIgnoreCase("none")) {
            return Proxy.NO_PROXY;
        } else if (pString.equalsIgnoreCase("PL")) {
            return AvarionProxy.PLChecker.BEST_PROXIES.get(ProxyUtils.random.nextInt(AvarionProxy.PLChecker.BEST_PROXIES.size()));
        } else if (pString.equalsIgnoreCase("TOP")) {
            return AvarionProxy.GLChecker.BEST_PROXIES.get(ProxyUtils.random.nextInt(AvarionProxy.GLChecker.BEST_PROXIES.size()));
        } else {
            if (pString.contains(":")) {
                final String[] sp = pString.split(":", 2);
                return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(sp[0], Integer.parseInt(sp[1])));
            }
        }
        return Proxy.NO_PROXY;
    }


}
