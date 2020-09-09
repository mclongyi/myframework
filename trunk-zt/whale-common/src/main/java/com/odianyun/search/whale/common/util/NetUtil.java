package com.odianyun.search.whale.common.util;

import java.net.*;
import java.util.Enumeration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class NetUtil {

	static Logger log = Logger.getLogger(NetUtil.class);

	private static String localIp="";

	public static String getLocalIP() {
		if(StringUtils.isNotBlank(localIp)){
			return localIp;
		}
		try {
			localIp = com.odianyun.architecture.caddy.common.utils.SystemUtil.getLocalhostIp();
		} catch (Exception e) {
			log.error(e.getMessage(), e);

		}
		return localIp;
	}
}
