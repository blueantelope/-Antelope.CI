// com.antelope.ci.bus.common.test.TestEnctyptUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common.test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.antelope.ci.bus.common.EncryptUtil;
import com.antelope.ci.bus.common.exception.CIBusException;

/**
 * test of EncryptUtil
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午9:11:28
 */
public class TestEnctyptUtil extends TestCase {
	private static final String TEST_SOURCE = "test.encrypt";
	private static final String TEST_SEED = "test.seed";

	@Test
	public void testMd5() throws CIBusException {
		String password = EncryptUtil.md5(TEST_SOURCE);
		System.out.println("md5 test: " + password);
	}

	@Test
	public void testDes() throws CIBusException {
//		String password = EncryptUtil.genDES(TEST_SEED, TEST_SOURCE);
		String source = EncryptUtil.decryptDES(TEST_SEED, "UKrt6PSVyWhkJZfSWdUwsg==");
		System.out.println("des test : source = " + source + ", password = "
				+ "G7x8VmdYH8IJ33n/7adIOQ==");
	}

	@Test
	public void test3Des() throws CIBusException {
		String password = EncryptUtil.gen3DES(TEST_SEED, TEST_SOURCE);
		String source = EncryptUtil.decrypt3DES(TEST_SEED, password);
		System.out.println("3des test : source = " + source + ", password = "
				+ password);
	}

	@Test
	public void testMyDes() {
		try {

			SecretKey myDesKey = buildDesKey();
			Cipher desCipher;

			// Create the cipher
			desCipher = Cipher.getInstance("DES");

			// Initialize the cipher for encryption
			desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);

			// sensitive information
			byte[] text = "No body can see me".getBytes();

			System.out.println("Text [Byte Format] : " + text);
			System.out.println("Text : " + new String(text));

			// Encrypt the text
			byte[] textEncrypted = desCipher.doFinal(text);
			String p = Base64.encodeBase64String(textEncrypted);
			System.out.println("Text Encryted : " + p);

			// Initialize the same cipher for decryption
			desCipher.init(Cipher.DECRYPT_MODE, myDesKey);

			// Decrypt the text
			byte[] textDecrypted = desCipher.doFinal(Base64.decodeBase64(p.getBytes()));

			System.out.println("Text Decryted : " + new String(textDecrypted));

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
	
	private SecretKey buildDesKey() throws NoSuchAlgorithmException {
		KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
		keygenerator.init(new SecureRandom(TEST_SEED.getBytes()));
		SecretKey myDesKey = keygenerator.generateKey();
		return myDesKey;
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestEnctyptUtil.class);
	}
}
