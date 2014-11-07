// com.antelope.ci.bus.common.test.TestEncryptUtilForAsymmetric.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import java.io.File;
import java.security.KeyPair;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.EncryptUtil;
import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.FileUtil;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
s *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午3:26:43 
 */
public class TestEncryptUtilForAsymmetric extends TestCase {
	private static final String password = "password";
	
	@Test
	public void testRsaCipher() throws CIBusException {
		KeyPair rsa_key = EncryptUtil.genKeyPair(ASYMMETRIC_ALGORITHM._RSA, -1);
		String private_cipher = EncryptUtil.genPrivateCipher(rsa_key.getPrivate(), null, -1);
		String public_cipher = EncryptUtil.genPublicCipher(rsa_key.getPublic(), "blueantelope@localhost", -1);
		String pwd_private_cipher = EncryptUtil.genPrivateCipher(rsa_key.getPrivate(), password, -1);
		System.out.println("private key : \n" + private_cipher);
		System.out.println("public key : \n" + public_cipher);
		System.out.println("private password key : \n" + pwd_private_cipher);
	}
	
	@Test
	public void testDsaCipher() throws CIBusException {
		KeyPair dsa_key = EncryptUtil.genKeyPair(ASYMMETRIC_ALGORITHM._DSA, -1);
		String private_cipher = EncryptUtil.genPrivateCipher(dsa_key.getPrivate(), null, -1);
		String public_cipher = EncryptUtil.genPublicCipher(dsa_key.getPublic(), "blueantelope@localhost", -1);
		String pwd_private_cipher = EncryptUtil.genPrivateCipher(dsa_key.getPrivate(), password, -1);
		System.out.println("private key : \n" + private_cipher);
		System.out.println("public key : \n" + public_cipher);
		System.out.println("private password key : \n" + pwd_private_cipher);
	}
	
	@Test
	public void testWriteRsa() throws CIBusException {
		String temp_dir = System.getProperty("java.io.tmpdir");
		File keys_dir = new File(temp_dir+File.separator+"keys");
		if (keys_dir.exists())
			FileUtil.delFolder(keys_dir.toString());
		keys_dir.mkdir();
		System.out.println("keys dir is " + keys_dir.getPath());
		KeyPair rsa_key = EncryptUtil.genKeyPair(ASYMMETRIC_ALGORITHM._RSA, -1);
		EncryptUtil.writePrivate(keys_dir.getPath()+File.separator+"test.pem", rsa_key.getPrivate(), null, EncryptUtil.VENDOR_SECURECRT);
		EncryptUtil.writePublic(
				keys_dir.getPath()+File.separator+"test.pem.pub", 
				rsa_key.getPublic(), 
				"blueantelope@localhost", 
				EncryptUtil.VENDOR_SECURECRT
				);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestEncryptUtilForAsymmetric.class);
	}
}

