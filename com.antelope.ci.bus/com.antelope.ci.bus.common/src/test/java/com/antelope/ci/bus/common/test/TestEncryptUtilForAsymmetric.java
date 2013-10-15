// com.antelope.ci.bus.common.test.TestEncryptUtilForAsymmetric.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common.test;

import java.security.KeyPair;

import junit.framework.TestCase;

import org.junit.Test;

import com.antelope.ci.bus.common.EncryptUtil;
import com.antelope.ci.bus.common.EncryptUtil.ASYMMETRIC_ALGORITHM;
import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * TODO 描述
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-10-15		下午3:26:43 
 */
public class TestEncryptUtilForAsymmetric extends TestCase {
	private static final String password = "password";
	
	@Test
	public void testRsaCipher() throws CIBusException {
		KeyPair rsa_key = EncryptUtil.generateKeyPair(ASYMMETRIC_ALGORITHM._RSA, 1024);
		String private_cipher = EncryptUtil.genPrivateCipher(rsa_key.getPrivate());
		String public_cipher = EncryptUtil.genPublicCipher(rsa_key.getPublic(), "blueantelope", "localhost");
		String pwd_private_cipher = EncryptUtil.genPrivateCipher(rsa_key.getPrivate(), password);
		System.out.println("private key : \n" + private_cipher);
		System.out.println("public key : \n" + public_cipher);
		System.out.println("private password key : \n" + pwd_private_cipher);
	}
	
	@Test
	public void testDsaCipher() throws CIBusException {
		KeyPair dsa_key = EncryptUtil.generateKeyPair(ASYMMETRIC_ALGORITHM._DSA, 1024);
		String private_cipher = EncryptUtil.genPrivateCipher(dsa_key.getPrivate());
		String public_cipher = EncryptUtil.genPublicCipher(dsa_key.getPublic(), "blueantelope", "localhost");
		String pwd_private_cipher = EncryptUtil.genPrivateCipher(dsa_key.getPrivate(), password);
		System.out.println("private key : \n" + private_cipher);
		System.out.println("public key : \n" + public_cipher);
		System.out.println("private password key : \n" + pwd_private_cipher);
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestEncryptUtilForAsymmetric.class);
	}
}

