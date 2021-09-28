package com.demo.learn.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author luwt
 * @date 2021/9/28
 */
public class IPUtil {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress host = InetAddress.getLocalHost();
        System.out.println(host.getHostAddress());
    }

}
