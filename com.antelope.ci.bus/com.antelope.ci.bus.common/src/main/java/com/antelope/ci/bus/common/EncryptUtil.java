// com.antelope.ci.bus.common.ARSecurityUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * 加密算法工具类
 * md5, des, 3des
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午5:54:04
 */
public class EncryptUtil {
	private static final Logger log = Logger.getLogger(EncryptUtil.class);
	
	// 对称加密算法定义
	public enum SYMMETRIC_ALGORITHM {
		_ORIGIN("origin"),
		_MD5("md5"),
		_DES("des"),
		_3DES("3des");
		private String name;
		private SYMMETRIC_ALGORITHM(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static SYMMETRIC_ALGORITHM toAlgorithm(String name) {
			for (SYMMETRIC_ALGORITHM c : SYMMETRIC_ALGORITHM.values()) {
				if (name.trim().equalsIgnoreCase(c.getName())) {
					return c;
				}
			}
			
			return null;
		}
	};
	
	// 非对称加密算法
	public enum  ASYMMETRIC_ALGORITHM {
		_DSA("dsa"),
		_RSA("rsa");
		private String name;
		private ASYMMETRIC_ALGORITHM(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String toString() {
			return name;
		}
		public static ASYMMETRIC_ALGORITHM toCipher(String name) {
			for (ASYMMETRIC_ALGORITHM c : ASYMMETRIC_ALGORITHM.values()) {
				if (name.trim().equalsIgnoreCase(c.getName())) {
					return c;
				}
			}
			
			return null;
		}
	}
	
	private static final int PAD_BELOW = 0x10;
	private static final int TWO_BYTES = 0xFF;
	private static final int PEM_RSA_PRIVATE_KEY = 1;
	private static final int PEM_DSA_PRIVATE_KEY = 2;

	private static final String ALGORITHM_MD5 = "MD5";
	private static final String ALGORITHM_DES = "DES";
	private static final String ALGORITHM_3DES = "DESede";
	private static final String ALGORITHM_DSA = "DSA";
	private static final String ALGORITHM_RSA = "RSA";

	/**
	 * 对称式算法加密
	 * @throws CIBusException 
	 * @param  @param algorithm
	 * @param  @param seed
	 * @param  @param source
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String encrypt_symmetric(SYMMETRIC_ALGORITHM algorithm, String seed, String source) throws CIBusException {
		switch (algorithm) {
			case _ORIGIN:
				return source;
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
	 * 对称式算法解密
	 * @throws CIBusException 
	 * @param  @param algorithm
	 * @param  @param seed
	 * @param  @param password
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String decrypt_symmetric(SYMMETRIC_ALGORITHM algorithm, String seed, String password) throws CIBusException {
		switch (algorithm) {
			case _ORIGIN:
			case _MD5:
				return password;
			case _DES:
				return decryptDES(seed, password);
			case _3DES:
				return decrypt3DES(seed, password);
			default:
				throw new CIBusException("000", "unknow algorithm");
		}
	}
	
	/**
	 * 非对称式密钥验证
	 * @param  @param publicKey
	 * @param  @param signText
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return boolean
	 * @throws
	 */
	public static boolean verify_asymmetric(PublicKey publicKey, String signText) throws CIBusException {
		String algorithm = publicKey.getAlgorithm();
		ASYMMETRIC_ALGORITHM cipher = ASYMMETRIC_ALGORITHM.toCipher(algorithm);
		byte[] signed = Base64.decode(signText.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(signed);  
		KeyFactory keyFactory = null;
		switch (cipher) {
			case _DSA:
				try {
					keyFactory = KeyFactory.getInstance(ALGORITHM_DSA);
					break;
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					throw new CIBusException("", e);
				}
			case _RSA:
				try {
					keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
					break;
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
					throw new CIBusException("", e);
				}
			default:
				throw new CIBusException("000", "unknow algorithm");
		}
		if (keyFactory != null) {
			try {
				PublicKey pubKey = keyFactory.generatePublic(keySpec);
				if (pubKey.equals(publicKey)) {
					return true;
				}
				return false;
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
				throw new CIBusException("", e);
			}
		}
		throw new CIBusException("000", "Unsupported algorithm");
	}
	
	/**
	 * 产生私钥密文
	 * @throws CIBusException 
	 * @param  @param privateKey
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String genPrivateCipher(PrivateKey privateKey) throws CIBusException {
		StringBuffer buff = new StringBuffer();
		String algorithm = privateKey.getAlgorithm().toUpperCase();
		String head = "-----BEGIN " + algorithm + " PRIVATE KEY-----\n";
		String tail = "-----END " + algorithm + " PRIVATE KEY-----\n";
		buff.append(head);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		String content = new String(Base64.encode(spec.getEncoded()));
		for (int n = 0; n<content.length()/64; n++) {
			buff.append(content.substring(n*64, (n+1)*64)).append("\n");
		}
		buff.append(tail);
		return buff.toString();
	}
	
	/**
	 * 产生私钥密文并经过密码再次加密
	 * @param  @param privateKey
	 * @param  @param password
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return String
	 * @throws
	 */
	public static String genPrivateCipher(PrivateKey privateKey, String password) throws CIBusException {
		StringBuffer buff = new StringBuffer();
		String algorithm = privateKey.getAlgorithm().toUpperCase();
		String head = "-----BEGIN " + algorithm + " PRIVATE KEY-----\n";
		String tail = "-----END " + algorithm + " PRIVATE KEY-----\n";
		buff.append(head);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
		String content = new String(Base64.encode(passwordEncrypt(password, spec.getEncoded())));
		for (int n = 0; n<content.length()/64; n++) {
			buff.append(content.substring(n*64, (n+1)*64)).append("\n");
		}
		buff.append(tail);
		return buff.toString();
	}
	
	/**
	 * 产生公钥密文
	 * @param  @param publicKey
	 * @param  @param username
	 * @param  @param host
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String genPublicCipher(PublicKey publicKey, String username, String host) {
		StringBuffer buff = new StringBuffer();
		String algorithm = publicKey.getAlgorithm().toLowerCase();
		String head = "ssh-" + algorithm + " ";
		String tail = " " + username + "@" + host;
		buff.append(head);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
		String content = new String(Base64.encode(spec.getEncoded()));
		buff.append(content);
		buff.append(tail);
		return buff.toString();
	}
	
	/*
	 * 使用密码加密
	 */
	private static byte[] passwordEncrypt(String password, byte[] plaintext) throws CIBusException {
		try {
		    int MD5_ITERATIONS = 1000;
		    byte[] salt = new byte[8];
		    SecureRandom random = new SecureRandom();
		    random.nextBytes(salt);
	
		    PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithSHAAndTwofish-CBC");
		    SecretKey key = keyFactory.generateSecret(keySpec);
		    PBEParameterSpec paramSpec = new PBEParameterSpec(salt, MD5_ITERATIONS);
		    Cipher cipher = Cipher.getInstance("PBEWithSHAAndTwofish-CBC");
		    cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	
		    byte[] ciphertext = cipher.doFinal(plaintext);
	
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    baos.write(salt);
		    baos.write(ciphertext);
		    return baos.toByteArray();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	/**
	 * 根据算法产生密钥对
	 * @param  @param algorithm
	 * @param  @param keySize
	 * @param  @return
	 * @return KeyPair
	 * @throws
	 */
	public static KeyPair generateKeyPair(ASYMMETRIC_ALGORITHM algorithm, int keySize) {
		return generateKeyPair(algorithm.getName(), keySize);
	}

	// 产生密钥对
	private static KeyPair generateKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
            SecureRandom random = new SecureRandom();
            if (keySize != 0) {
                generator.initialize(keySize, random);
            }
            KeyPair kp = generator.generateKeyPair();
            return kp;
        } catch (Exception e) {
            log.error("Unable to generate keypair", e);
            return null;
        }
    }

	// 生成MD5密码
	public final static String md5(String s) throws CIBusException {
		byte[] btInput = s.getBytes();
		MessageDigest mdInst = null;
		try {
			mdInst = MessageDigest.getInstance(ALGORITHM_MD5);
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
			SecretKey key = buildDESKey(seed);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] srcBs = cipher.doFinal(source.getBytes());
			return new String(Base64.encode(srcBs));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// DES解密
	public static String decryptDES(String seed, String password)
			throws CIBusException {
		try {
			SecretKey key = buildDESKey(seed);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decoder = Base64.decode(password.getBytes());
			return new String(cipher.doFinal(decoder));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	/*
	 * 产生DES密码
	 */
	private final static SecretKey buildDESKey(String seed) throws CIBusException {
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
			byte[] srcBs = cipher.doFinal(source.getBytes());
			return new String(Base64.encode(srcBs));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 3DES解密
	public static String decrypt3DES(String seed, String password) throws CIBusException {
		try {
			SecretKey _3deskey = new SecretKeySpec(build3DesKey(seed), ALGORITHM_3DES);
			Cipher cipher = Cipher.getInstance(ALGORITHM_3DES);
			cipher.init(Cipher.DECRYPT_MODE, _3deskey); // 初始化为解密模式
			byte[] decoder = Base64.decode(password.getBytes());
			return new String(cipher.doFinal(decoder));
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 产生3DES密码
	public static byte[] build3DesKey(String keyStr)
			throws UnsupportedEncodingException {
		byte[] key = new byte[24];
		byte[] temp = keyStr.getBytes();
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
