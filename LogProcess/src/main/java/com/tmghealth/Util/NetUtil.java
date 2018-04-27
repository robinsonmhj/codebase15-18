package com.tmghealth.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class NetUtil {

	private static Logger log= Logger.getLogger(NetUtil.class);
	public static String getHostName() {

		String hostName = "Unknown";

		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (UnknownHostException ex) {
			log.error("cannot get hostName",ex);
		}
		
		return hostName;
		

	}

}
