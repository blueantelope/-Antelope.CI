// com.antelope.ci.bus.common.ARSecurityUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 加密算法工具类
 * md5, des, 3des
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午5:54:04
 */
public class EncryptUtil {
	public enum CIPHER { 
		_MD5("md5"),
		_DES("des"),
		_3DES("3des");
		private String name;
		private CIPHER(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static CIPHER toCipher(String name) {
			for (CIPHER c : CIPHER.values()) {
				if (name.trim().equalsIgnoreCase(c.getName())) {
					return c;
				}
			}
			
			return null;
		}
	};
	
	/**
	 * @throws CIBusException 
	 * 根据算法加密
	 * @param  @param cipher
	 * @param  @param seed
	 * @param  @param source
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String encrypt(CIPHER cipher, String seed, String source) throws CIBusException {
		switch (cipher) {
			case _MD5:
				return md5(source);
			case _DES:
				return genDES(seed, source);
			case _3DES:
				return gen3DES(seed, source);
			default:
				throw new CIBusException("000", "unknow algorithm");
		}
	}
	
	/**
	 * @throws CIBusException 
	 * 根据算法解密
	 * @param  @param cipher
	 * @param  @param seed
	 * @param  @param password
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String decrypt(CIPHER cipher, String seed, String password) throws CIBusException {
		switch (cipher) {
			case _DES:
				return decryptDES(seed, password);
			case _3DES:
				return decrypt3DES(seed, password);
			default:
				throw new CIBusException("000", "unknow algorithm");
		}
	}
	
	
	private static final int PAD_BELOW = 0x10;
	private static final int TWO_BYTES = 0xFF;

	private static final String ALGORITHM_DES = "DES";
	private static final String ALGORITHM_3DES = "DESede";

	// 生成MD5密码
	public final static String md5(String s) throws CIBusException {
		byte[] btInput = s.getBytes();
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new CIBusException("", e);

		}
		mdInst.update(btInput);
		byte[] md = mdInst.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < md.length; i++) {
			int val = ((int) md[i]) & 0xff;
			if (val < 16)
				sb.append("0");
			sb.append(Integer.toHexString(val));

		}
		return sb.toString();
	}

	// 产生DES密码
	public final static String genDES(String seed, String source)
			throws CIBusException {
		try {
			SecretKey key = genDESKey(seed);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] srcBs = cipher.doFinal(source.getBytes("UTF-8"));
			return Base64.encodeBase64String(srcBs);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// DES解密
	public static String decryptDES(String seed, String password)
			throws CIBusException {
		try {
			SecretKey key = genDESKey(seed);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decoder = Base64.decodeBase64(password.getBytes("UTF-8"));
			return new String(cipher.doFinal(decoder));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	/*
	 * 产生DES密码
	 */
	private final static SecretKey genDESKey(String seed) throws CIBusException {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_DES);
			keyGen.init(new SecureRandom(seed.getBytes()));
			return keyGen.generateKey();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 3DES加密
	public static String gen3DES(String seed, String source) throws CIBusException {
		try {
			SecretKey _3deskey = new SecretKeySpec(build3DesKey(seed), ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance(ALGORITHM_3DES);
			cipher.init(Cipher.ENCRYPT_MODE, _3deskey);
			byte[] srcBs = cipher.doFinal(source.getBytes("UTF-8"));
			return Base64.encodeBase64String(srcBs);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 3DES解密
	public static String decrypt3DES(String seed, String password) throws CIBusException {
		try {
			SecretKey deskey = new SecretKeySpec(build3DesKey(seed), ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance(ALGORITHM_3DES);
			cipher.init(Cipher.DECRYPT_MODE, deskey); // 初始化为解密模式
			byte[] decoder = Base64.decodeBase64(password.getBytes("UTF-8"));
			return new String(cipher.doFinal(decoder));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 产生3DES密码
	public static byte[] build3DesKey(String keyStr)
			throws UnsupportedEncodingException {
		byte[] key = new byte[24];
		byte[] temp = keyStr.getBytes("UTF-8");
		if (key.length > temp.length) {
			System.arraycopy(temp, 0, key, 0, temp.length);
		} else {
			System.arraycopy(temp, 0, key, 0, key.length);
		}
		return key;
	}

	// 产生ID
	public static String genId(String[] args) {
		String id;
		// 加密信息
		StringBuffer insertMD5 = new StringBuffer();
		String time = Long.toString(System.currentTimeMillis()); // 时间
		String rand = Long.toString(new SecureRandom().nextLong()); // 随机数
		insertMD5.append(time).append(rand);
		for (String arg : args)
			insertMD5.append(arg);

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			String beforMD5 = insertMD5.toString();
			md5.update(beforMD5.getBytes());
			byte[] bs = md5.digest();
			StringBuffer afterMD5 = new StringBuffer();
			for (int b : bs) {
				b = b & TWO_BYTES;
				if (b < PAD_BELOW)
					afterMD5.append('0');
				afterMD5.append(Integer.toHexString(b));
			}
			id = afterMD5.toString();
		} catch (NoSuchAlgorithmException e) {
			String uuid = UUID.fromString(insertMD5.toString()).toString();
			byte[] uuidBuffer = new byte[32];
			int n = 0;
			for (byte b : uuid.getBytes()) {
				if ((char) b != '-')
					uuidBuffer[n++] = b;
				if (n == 32)
					break;
			}

			id = new String(uuidBuffer);
		}

		return id;
	}

}
