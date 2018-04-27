/**
 * 
 */
package com.multiplemedia.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

/**
 * @author Haojie Ma
 * @date Dec 14, 2015
 */
public class CommonUtil {

	private static Logger log = Logger.getLogger(CommonUtil.class);

	public static String md5(String password) {

		if (StringUtils.isEmpty(password))
			return null;

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());

			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (Exception e) {

			log.info("", e);

		}

		return null;
	}

	public static void main(String[] args) {

		try {
			String encryped = md5("123456");
			System.out.println(encryped);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

}
