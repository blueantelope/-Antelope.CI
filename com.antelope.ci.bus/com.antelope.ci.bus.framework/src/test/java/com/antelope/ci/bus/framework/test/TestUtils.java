// com.antelope.ci.bus.framework.test.TestUtils.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.framework.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

import junit.framework.TestCase;

import org.junit.Test;
import org.osgi.framework.Constants;

import com.antelope.ci.bus.framework.test.inner.TestBundleActivator;
import com.antelope.ci.bus.framework.test.inner.TestClass;

/**
 * 测试工具类
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2013-9-30 下午3:36:14
 */
public class TestUtils extends TestCase {
	public static Method getMethod(Class clazz, String methodName,
			final Class[] classes) throws Exception {
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, classes);
		} catch (NoSuchMethodException e) {
			try {
				method = clazz.getMethod(methodName, classes);
			} catch (NoSuchMethodException ex) {
				if (clazz.getSuperclass() == null) {
					return method;
				} else {
					method = getMethod(clazz.getSuperclass(), methodName,
							classes);
				}
			}
		}
		return method;
	}

	public static Object invoke(final Object obj, final String methodName,
			final Class[] classes, final Object[] objects) {
		try {
			Method method = getMethod(obj.getClass(), methodName, classes);
			method.setAccessible(true);// 调用private方法的关键一句话
			return method.invoke(obj, objects);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz, Object[] args) {
		try {
			Constructor c;
			if (args.length > 0) {
				Class[] args_cls = new Class[args.length];
				int n = 0;
				for (Object arg : args)
					args_cls[n++] = arg.getClass();
				c = clazz.getDeclaredConstructor(args_cls);
			} else {
				c = clazz.getDeclaredConstructor();
			}

			c.setAccessible(true);
			T t = (T) c.newInstance(args);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Test
	public void test_newInstance() throws ClassNotFoundException {
		Object[] args = new Object[] { new String(), new String() };
		Class clazz = Class
				.forName("com.antelope.ci.bus.framework.test.inner.PackageClass");
		newInstance(clazz, new Object[] {});
	}

	public static File createBundle() throws IOException {
		return createBundle(TestBundleActivator.class);
	}
	
	public static File createBundle(Class clazz) throws IOException {
		String manifest = "Bundle-SymbolicName: boot.test\n"
	            + "Bundle-Version: 1.1.0\n"
	            + "Bundle-ManifestVersion: 2\n"
	            + "Import-Package: org.osgi.framework\n";
		File f = File.createTempFile("felix-bundle", ".jar");
		f.deleteOnExit();

		Manifest mf = new Manifest(new ByteArrayInputStream(manifest.getBytes("utf-8")));
		mf.getMainAttributes().putValue("Manifest-Version", "1.0");
		mf.getMainAttributes().putValue(Constants.BUNDLE_ACTIVATOR, clazz.getName());
		JarOutputStream os = new JarOutputStream(new FileOutputStream(f), mf);

		String path = clazz.getName().replace('.', '/') + ".class";
		os.putNextEntry(new ZipEntry(path));

		InputStream is = clazz.getClassLoader().getResourceAsStream(path);
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		os.write(b);

		os.close();
		return f;
	}
	
	public static void modifyField(Object instance, String name, Object value) throws Exception {
		Field field = instance.getClass().getDeclaredField(name);
		field.setAccessible(true);
		field.set(instance, value);
	}
	
	public void test_modifyField() throws Exception {
		modifyField(new TestClass(), "i", 2);
	}
	
	public static Object getField(Object instance, String name) throws Exception {
		Field field = instance.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return field.get(instance);
	}
	
	public void test_modifyAndgetField() throws Exception {
		TestClass t = new TestClass();
		int i = (Integer) getField(t, "i");
		System.out.println("before i = " + i);
		modifyField(t, "i", 2);
		i = (Integer) getField(t, "i");
		System.out.println("after i = " + i);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TestUtils.class);
	}
}
