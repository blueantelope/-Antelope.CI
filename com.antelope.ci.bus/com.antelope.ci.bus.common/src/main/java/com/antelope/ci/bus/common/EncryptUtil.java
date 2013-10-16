// com.antelope.ci.bus.common.ARSecurityUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
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
	private static final String KEYALIAS = "cibus.key";
	private static final int VENDOR = 1; 
	public static final int VENDOR_OPENSSH = 1; 
	public static final int VENDOR_SECURECRT= 2;
	public static final int VENDOR_PUTTY = 3;
	public static final int VENDOR_FSECURE = 4;
	private static final String CR = "\n";
	private static final String[] PASSWORD_HEADER ={"Proc-Type: 4,ENCRYPTED",  "DEK-Info: DES-EDE3-CBC,"};

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
					throw new CIBusException("", e);
				}
			case _RSA:
				try {
					keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
					break;
				} catch (NoSuchAlgorithmException e) {
					throw new CIBusException("", e);
				}
			default:
				throw new CIBusException("000", "Unknow algorithm");
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
	 * 将私钥保存为文件
	 * @throws CIBusException 
	 * @param  @param filename
	 * @param  @param privateKey
	 * @param  @param password
	 * @param  @param vendor
	 * @return void
	 * @throws
	 */
	public static void writePrivate(String filename, PrivateKey privateKey, String password, int vendor) throws CIBusException {
		String private_cipher = genPrivateCipher(privateKey, password, vendor);
		FileUtil.newFile(filename, private_cipher);
	}
	
	/**
	 * 产生私钥密文并经过密码再次加密
	 * @param  @param privateKey
	 * @param  @param password -1无密码
	 * @param  @param vendor	-1默认格式
	 * @param  @return
	 * @param  @throws CIBusException
	 * @return String
	 * @throws
	 */
	public static String genPrivateCipher(PrivateKey privateKey, String password, int vendor) throws CIBusException {
		StringBuffer buff = new StringBuffer();
		String algorithm = privateKey.getAlgorithm().toUpperCase();
		String head = "-----BEGIN " + algorithm + " PRIVATE KEY-----" + CR;
		String tail = "-----END " + algorithm + " PRIVATE KEY-----" + CR;
		
		byte[] plain=  new X509EncodedKeySpec(privateKey.getEncoded()).getEncoded();
		byte[][] _iv=new byte[1][];
		byte[] encoded = enryptWithPassword(plain, _iv, password, vendor);
		if(encoded!=plain)
			bzero(plain);
		byte[] iv =_iv[0];
		String content = new String(Base64.encode(encoded));
		
		buff.append(head);
		if (password != null) {
			buff.append(PASSWORD_HEADER[0]+CR);
			buff.append(PASSWORD_HEADER[1]);
			for(int i=0; i<iv.length; i++) {
				buff.append(b2a((byte)((iv[i]>>>4)&0x0f)));
				buff.append(b2a((byte)(iv[i]&0x0f)));
			}
			buff.append(CR);
			buff.append(CR);
		}
		
		buff.append(styleLine(content, 64, ""));
		buff.append(tail);
		return buff.toString();
	}
	
	/**
	 * 将公钥写入文件
	 * @param  @param filename
	 * @param  @param publicKey
	 * @param  @param comment
	 * @param  @param vendor
	 * @param  @throws CIBusException
	 * @return void
	 * @throws
	 */
	public static void writePublic(String filename, PublicKey publicKey, String comment, int vendor) throws CIBusException {
		String public_cipher = genPublicCipher(publicKey, comment, vendor);
		FileUtil.newFile(filename, public_cipher);
	}
	
	/**
	 * 产生公钥密文
	 * @throws CIBusException 
	 * @param  @param publicKey
	 * @param  @param comment null无注释
	 * @param  @param vendor -1默认格式
	 * @param  @return
	 * @return String
	 * @throws
	 */
	public static String genPublicCipher(PublicKey publicKey, String comment, int vendor) throws CIBusException {
		if (vendor == -1)
			vendor = VENDOR;
		StringBuffer buff = new StringBuffer();
		String algorithm = publicKey.getAlgorithm();
		X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKey.getEncoded());
		String content = new String(Base64.encode(spec.getEncoded()));
		switch (vendor) {
			case VENDOR_OPENSSH:
			case VENDOR_PUTTY:
			case VENDOR_FSECURE:
				buff.append("ssh-").append(algorithm.toLowerCase()).append(" ");
				buff.append(content);
				if (comment != null)
					buff.append(" ").append(comment);
				return buff.toString();
			case VENDOR_SECURECRT:
				buff.append("---- BEGIN SSH2 PUBLIC KEY ----").append(CR);
				StringBuffer comm_buff = new StringBuffer();
				int keysize = 1024;
				switch (ASYMMETRIC_ALGORITHM.toCipher(algorithm)) {
					case _RSA:
						keysize = ((RSAPublicKey) publicKey).getModulus().bitLength();
						break;
				}
				comm_buff.append("Comment: ")
					.append("\"")
					.append(keysize)
					.append("-bit ")
					.append(algorithm)
					.append(", converted from @Antelope.CI.BUS")
					.append(comment==null?"":" by "+comment)
					.append("\"");
				buff.append(styleLine(comm_buff.toString(), 64, " \\ "));
				buff.append(styleLine(content, 70, ""));
				buff.append("---- END SSH2 PUBLIC KEY ----").append(CR);
				return buff.toString();
			default:
				throw new CIBusException("", "Unknow vendor");
		}
	}
	
	/**
	 * 根据算法产生密钥对
	 * @throws CIBusException 
	 * @param  @param algorithm
	 * @param  @param keySize
	 * @param  @return
	 * @return KeyPair
	 * @throws
	 */
	public static KeyPair genKeyPair(ASYMMETRIC_ALGORITHM algorithm, int keySize) throws CIBusException {
		return genKeyPair(algorithm.getName(), keySize);
	}
	
	private static byte b2a(byte c) {
	    if(0<=c&&c<=9) return (byte)(c+'0');
	    return (byte)(c-10+'A');
	}
	
	private static String styleLine(String content, int line_len, String line_end) {
		StringBuffer buff = new StringBuffer();
		int n = 0;
		for (; n<content.length()/line_len; n++) {
			buff.append(content.substring(n*line_len, (n+1)*line_len)).append(line_end).append(CR);
		}
		if (content.length() > (n*line_len)) {
			buff.append(content.substring(n*line_len, content.length())).append(CR);
		}
		
		return buff.toString();
	}
	
	@Deprecated
	private static byte[] passwordEncrypt(String password, byte[] plaintext) throws CIBusException {
		try {
			String MYPBEALG = "PBEWithSHA1AndDESede";
			int count = 20;// hash iteration count
			byte[] salt = generateSalt();
			// Create PBE parameter set
			PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, count);
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFac = SecretKeyFactory.getInstance(MYPBEALG);
			SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

			Cipher pbeCipher = Cipher.getInstance(MYPBEALG);

			// Initialize PBE Cipher with key and parameters
			pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);

			// Encrypt the encoded Private Key with the PBE key
			byte[] ciphertext = pbeCipher.doFinal(plaintext);

			// Now construct  PKCS #8 EncryptedPrivateKeyInfo object
			AlgorithmParameters algparms = AlgorithmParameters.getInstance(MYPBEALG);
			algparms.init(pbeParamSpec);
			EncryptedPrivateKeyInfo encinfo = new EncryptedPrivateKeyInfo(algparms, ciphertext);

			// and here we have it! a DER encoded PKCS#8 encrypted key!
			return encinfo.getEncoded();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	private static byte[] generateSalt() throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		random.nextBytes(salt);

		return salt;
	}
	
	/*
	 * 使用密码加密
	 */
	private static byte[] enryptWithPassword(byte[] plain, byte[][] _iv, String password, int vendor) throws CIBusException {
		if (password == null) 
			return plain;
		byte[] passphrase = password.getBytes();
		TripleDESCBC cipher = new TripleDESCBC();
		byte[] iv=_iv[0] = new byte[cipher.getIVSize()];
		Random random = new Random();
		random.fill(iv, 0, iv.length);
		byte[] key = genKey(vendor, cipher, passphrase, iv);
	    byte[] encoded = plain;
	    // PKCS#5Padding
	    int bsize=cipher.getIVSize();
	    byte[] foo=new byte[(encoded.length/bsize+1)*bsize];
	    System.arraycopy(encoded, 0, foo, 0, encoded.length);
    	int padding=bsize-encoded.length%bsize;
    	for(int i=foo.length-1; (foo.length-padding)<=i; i--){
    		foo[i]=(byte)padding;
    	}
    	encoded=foo;
	    
    	try {
			cipher.init(Cipher.ENCRYPT_MODE, key, iv);
			cipher.update(encoded, 0, encoded.length, encoded, 0);
    	} catch (Exception e) {
			throw new CIBusException("", e);
		}
    	bzero(key);
    	return encoded;
	}
	
	private static void bzero(byte[] foo){
		if(foo==null)
			return;
	    for(int i=0; i<foo.length; i++)
	    	foo[i]=0;
	}
	
	private static byte[] genKey(int vendor, TripleDESCBC cipher, byte[] passphrase, byte[] iv) throws CIBusException {
		try {
			if (vendor == -1)
				vendor = VENDOR;
			MessageDigest md5 = MessageDigest.getInstance("MD5"); 
			byte[] key = new byte[cipher.getBlockSize()];
			int hsize = 16;
			byte[] hn = new byte[key.length / hsize * hsize
					+ (key.length % hsize == 0 ? 0 : hsize)];
			byte[] tmp = null;
			switch (vendor) {
				case VENDOR_OPENSSH:
				case VENDOR_SECURECRT:
					for (int index = 0; index + hsize <= hn.length;) {
						if (tmp != null) {
							md5.update(tmp, 0, tmp.length);
						}
						md5.update(passphrase, 0, passphrase.length);
						md5.update(iv, 0, iv.length > 8 ? 8 : iv.length);
						tmp = md5.digest();
						System.arraycopy(tmp, 0, hn, index, tmp.length);
						index += tmp.length;
					}
					System.arraycopy(hn, 0, key, 0, key.length);
					break;
				case VENDOR_PUTTY:
					MessageDigest sha1 = MessageDigest.getInstance("SHA-1"); 
					tmp = new byte[4];
					key = new byte[20 * 2];
					for (int i = 0; i < 2; i++) {
						tmp[3] = (byte) i;
						sha1.update(tmp, 0, tmp.length);
						sha1.update(passphrase, 0, passphrase.length);
						System.arraycopy(sha1.digest(), 0, key, i * 20, 20);
					}
					break;
				case VENDOR_FSECURE:
					for (int index = 0; index + hsize <= hn.length;) {
						if (tmp != null) {
							md5.update(tmp, 0, tmp.length);
						}
						md5.update(passphrase, 0, passphrase.length);
						tmp = md5.digest();
						System.arraycopy(tmp, 0, hn, index, tmp.length);
						index += tmp.length;
					}
					System.arraycopy(hn, 0, key, 0, key.length);
					break;
				default:
					throw new CIBusException("", "Unknow vendor");
			}
			return key;
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	// 产生密钥对
	private static KeyPair genKeyPair(String algorithm, int keySize) throws CIBusException {
		if (ALGORITHM_DSA.equalsIgnoreCase(algorithm)) {
			keySize = 1024;
		} else  if (ALGORITHM_RSA.equalsIgnoreCase(algorithm)) {
			if (keySize == -1) {
				keySize = 2048;
			} else if (keySize < 768){
				keySize = 768;
			}
		} else {
			throw new CIBusException("", "Unsupported algorithm");
		}
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

	private static class TripleDESCBC {
		private static final int ivsize = 8;
		private static final int bsize = 24;
		private javax.crypto.Cipher cipher;

		public int getIVSize() {
			return ivsize;
		}

		public int getBlockSize() {
			return bsize;
		}

		public void init(int mode, byte[] key, byte[] iv) throws Exception {
			String pad = "NoPadding";
			// if(padding) pad="PKCS5Padding";
			byte[] tmp;
			if (iv.length > ivsize) {
				tmp = new byte[ivsize];
				System.arraycopy(iv, 0, tmp, 0, tmp.length);
				iv = tmp;
			}
			if (key.length > bsize) {
				tmp = new byte[bsize];
				System.arraycopy(key, 0, tmp, 0, tmp.length);
				key = tmp;
			}

			try {
				cipher = javax.crypto.Cipher.getInstance("DESede/CBC/" + pad);
				/*
				 * // The following code does not work on IBM's JDK 1.4.1
				 * SecretKeySpec skeySpec = new SecretKeySpec(key, "DESede");
				 * cipher.init((mode==ENCRYPT_MODE?
				 * javax.crypto.Cipher.ENCRYPT_MODE:
				 * javax.crypto.Cipher.DECRYPT_MODE), skeySpec, new
				 * IvParameterSpec(iv));
				 */
				DESedeKeySpec keyspec = new DESedeKeySpec(key);
				SecretKeyFactory keyfactory = SecretKeyFactory
						.getInstance("DESede");
				SecretKey _key = keyfactory.generateSecret(keyspec);
				cipher.init(
						(mode == javax.crypto.Cipher.ENCRYPT_MODE ? javax.crypto.Cipher.ENCRYPT_MODE
								: javax.crypto.Cipher.DECRYPT_MODE), _key,
						new IvParameterSpec(iv));
			} catch (Exception e) {
				cipher = null;
				throw e;
			}
		}

		public void update(byte[] foo, int s1, int len, byte[] bar, int s2)
				throws Exception {
			cipher.update(foo, s1, len, bar, s2);
		}

		public boolean isCBC() {
			return true;
		}
	}
	
	private static class Random{
		private byte[] tmp = new byte[16];
		private SecureRandom random = null;

		public Random() {
			random = new SecureRandom();
		}

		public void fill(byte[] foo, int start, int len) {
			if (len > tmp.length) {
				tmp = new byte[len];
			}
			random.nextBytes(tmp);
			System.arraycopy(tmp, 0, foo, start, len);
		}
	}
}
