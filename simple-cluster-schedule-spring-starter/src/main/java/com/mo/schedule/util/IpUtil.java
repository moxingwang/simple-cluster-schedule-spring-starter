package com.mo.schedule.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 23:48
 **/
public class IpUtil {
    public IpUtil() {
    }

    public static String getIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                Enumeration addresses = ni.getInetAddresses();

                while (addresses.hasMoreElements()) {
                    address = (InetAddress) addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }

            return null;
        } catch (Throwable var4) {
            return null;
        }
    }
}
