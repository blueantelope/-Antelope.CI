// com.antelope.ci.bus.common.ProxyUtil.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.antelope.ci.bus.common.exception.CIBusException;


/**
 * 反射代理工具类
 * @author blueantelope
 * @version 0.1
 * @Date 2013-10-14 下午5:31:02
 */
public class ProxyUtil {
	// 取得包下的所有类
	public static List<String> getPacketClass(String packageName) {
		List<String> classNames = new ArrayList<String>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		try {
			String resourceName = packageName.replaceAll("\\.", "/");
			URL url = loader.getResource(resourceName);
			File urlFile = new File(url.toURI());
			File[] files = urlFile.listFiles();
			for (File f : files)
				getClassName(packageName, f, classNames);
		} catch (URISyntaxException e) {

		}
		return classNames;
	}

	// 递归取得包下的类
	private static void getClassName(String packageName, File packageFile,
			List<String> list) {
		if (packageFile.isFile()) {
			list.add(packageName + "."
					+ packageFile.getName().replace(".class", ""));
		} else {
			File[] files = packageFile.listFiles();
			String tmPackageName = packageName + "." + packageFile.getName();
			for (File f : files) {
				getClassName(tmPackageName, f, list);
			}
		}
	}

	// 新建类的实例对像 如果指定ClassLoader由此loader来加载
	public static Object newObject(Class clazz, ClassLoader... loader) throws CIBusException {
		Object o = null;
		if (loader.length == 0) {
			try {
				o = clazz.newInstance();
			} catch (Exception e) {
				throw new CIBusException("", "create new object error: ", e);
			}
		} else {
			try {
				o = loader[0].loadClass(clazz.getName()).newInstance();
			} catch (Exception e) {
				throw new CIBusException("", "create new object error: ", e);
			}
		}

		return o;
	}

	// 代理 执行对象中的函数 带参数无返回值
	public static void invoke(Object obj, String function, Object[] args) throws CIBusException {
		try {
			Method method = getMethod(obj.getClass(), function);
			method.invoke(obj, args);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

	}
	
	public static void invokeSetter(Object obj, Method method, String parameter) throws CIBusException {
		try {
			Class pt = method.getParameterTypes()[0];
			Object[] args = new Object[1];
			if (String.class.isAssignableFrom(pt)) args[0] = parameter;
			else if (Byte.class.isAssignableFrom(pt)) args[0] = Byte.valueOf(parameter);
			else if (Short.class.isAssignableFrom(pt)) args[0] = Short.valueOf(parameter);
			else if (Integer.class.isAssignableFrom(pt)) args[0] = Integer.valueOf(parameter);
			else if (Long.class.isAssignableFrom(pt)) args[0] = Long.valueOf(parameter);
			else if (Float.class.isAssignableFrom(pt)) args[0] = Float.valueOf(parameter);
			else if (Double.class.isAssignableFrom(pt)) args[0] = Double.valueOf(parameter);
			else if (Date.class.isAssignableFrom(pt)) args[0] = new SimpleDateFormat().parse(parameter);
			method.invoke(obj, args);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

	}
	
	public static void invoke(Object obj, Method method, Object[] args) throws CIBusException {
		try {
			method.invoke(obj, args);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

	}

	// 代理 执行对象中的函数 无参数无返回值
	public static void invoke(Object obj, String function) throws CIBusException {
		try {
			Method method = obj.getClass().getMethod(function, null);
			method.invoke(obj);
		} catch (Exception e) {
			System.out.println("错误：" + function);
			throw new CIBusException("", e);
		}

	}
	
	// 代理 执行对象中的函数 带参数有返回值
	public static Object invokeRet(Object obj, String function, Object[] args) throws CIBusException {
		Object ret = null;
		try {
			Method method = getMethod(obj.getClass(), function, args);
			ret = method.invoke(obj, args);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

		return ret;
	}

	// 代理 执行对象中的函数 不带参数有返回值
	public static Object invokeRet(Object obj, String function) throws CIBusException {
		Object ret = null;
		try {
			Method method = obj.getClass().getMethod(function, null);
			ret = method.invoke(obj);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

		return ret;
	}

	// 代理执行对像中的方法
	public static Object invokeRet(Object obj, Method method) throws CIBusException {
		Object ret = null;
		try {
			ret = method.invoke(obj, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

		return ret;
	}

	// 代理 执行静态函数 无参数无返回值
	public static void staticInvoke(Class clazz, String function) throws CIBusException {
		try {
			Method method = clazz.getMethod(function, null);
			method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 代理 执行静态函数 有参数无返回值
	public static void staticInvoke(Class clazz, String function, Object[] args) throws CIBusException {
		try {
			Method method = getMethod(clazz, function, args);
			method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}

	// 代理 执行静态函数 无参数有返回值
	public static Object staticRetInvoke(Class clazz, String function) throws CIBusException {
		Object ret = null;
		try {
			Method method = clazz.getMethod(function, null);
			ret = method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}

		return ret;
	}

	// 代理 执行静态函数 有参数有返回值
	public static Object staticRetInvoke(Class clazz, String function, Object[] args) throws CIBusException {
		Object ret = null;
		try {
			Method method = getMethod(clazz, function, args);
			ret = method.invoke(null, args);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CIBusException("", e);
		}

		return ret;
	}
	
	// 取得带参方法
	public static Method getMethod(Class clazz, String function, Object[] args) throws Exception {
		int argLen = args.length;
		Method[] pubMs = clazz.getMethods();
		Method[] matchMs = new Method[pubMs.length];
		int n = 0;
		for (Method method : clazz.getMethods()) {
			if (function.equals(method.getName())
					&& method.getParameterTypes().length == argLen)
				matchMs[n++] = method;
		}

		for (Method method : matchMs) {
			if (method == null)
				break;

			n = 0;
			for (Class paraCls : method.getParameterTypes()) {
				if (paraCls.isAssignableFrom(args[n].getClass())) {
					n++;
				} else {
					break;
				}
			}
			if (n == args.length)
				return method;
		}

		return null;
	}

	// 取得函数
	private static Method getMethod(Class clazz, String function) {
		Method[] ms = clazz.getMethods();
		for (Method method : ms) {
			if (method.getName().equals(function)) {
				return method;
			}
		}

		return null;
	}

	/*
	 * 打印类函数信息
	 */
	private static void printMehtodInfo(Object obj) {
		Method[] ms = obj.getClass().getMethods();
		for (Method m : ms) {
			System.out.println("name:" + m.getName());
			Class[] cs = m.getParameterTypes();
			for (Class c : cs) {
				System.out.println(c.getName());
			}
		}

	}

	// 由getter方法取得setter方面
	public static Method getSetter(Class clazz, Method getter) {
		Method setter = null;
		String setMethod = "set" + StringUtil.trimStrUpper(getter.getName());
		Class arg = getter.getReturnType();
		if (arg.isEnum())
			arg = String.class;
		try {
			setter = clazz.getMethod(setMethod, arg);
		} catch (SecurityException e) {

		} catch (NoSuchMethodException e) {

		} finally {
			return setter;
		}
	}
	
	public static List<Method> getSetter(Class clazz) {
		List<Method> setterList = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.getName().startsWith("set") && method.getParameterTypes().length == 1)
				setterList.add(method);
		}
		
		return setterList;
	}

	// 取得泛型变量中的泛型类型
	public static Class[] getGenericsClass(Class clazz, String filedName) {
		int n = 0;
		Class[] cs = null;
		try {
			ParameterizedType pt = (ParameterizedType) clazz.getDeclaredField(
					filedName).getGenericType();
			Type[] types = pt.getActualTypeArguments();
			cs = new Class[types.length];
			for (Type type : types)
				cs[n++] = (Class) type;
		} catch (SecurityException e) {

		} catch (NoSuchFieldException e) {

		} finally {
			if (n == 0)
				return new Class[0];
			return cs;
		}
	}

	// 建立新的对像
	public static Object newObject(String className) {
		Object ret = null;
		try {
			ret = Class.forName(className).newInstance();
		} catch (Exception e) {
			DevAssistant.assert_exception(e);
		} finally {
			return ret;
		}
	}

	// 指定classLoader初始化对像
	public static Object newObject(String className, ClassLoader loader) {
		Object ret = null;
		try {
			ret = loader.loadClass(className).newInstance();
		} catch (Exception e) {
			DevAssistant.assert_exception(e);
		} finally {
			return ret;
		}
	}

	// 取得方法的返回类型是否为void
	public static boolean isVoid(Method method) {
		if ("void".equals(method.getReturnType().getName()))
			return true;

		return false;
	}

	// 对像序列化方式实现clone
	public static Object clone(Object object) {
		Object ret = null;
		try {
			PipedInputStream objIn = new PipedInputStream();
			OutputStream objOut = new PipedOutputStream(objIn);

			ObjectOutputStream objWriter = new ObjectOutputStream(objOut);
			objWriter.writeObject(object);

			ObjectInputStream objReader = new ObjectInputStream(objIn);
			ret = objReader.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			return ret;
		}
	}
	
	public static Object createObject(String className) throws CIBusException {
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	public static Object createObject(Class clazz, ClassLoader... loader) throws CIBusException {
		if (loader.length == 0) {
			try {
				return clazz.newInstance();
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		} else {
			try {
				return loader[0].loadClass(clazz.getName()).newInstance();
			} catch (Exception e) {
				throw new CIBusException("", e);
			}
		} 
	}
	
	public static Object convertString(Class clazz, String value) throws CIBusException {
		if (value == null)
			return null;
		if (clazz.isAssignableFrom(String.class))			// String
			return value;
		if (clazz.isAssignableFrom(Integer.class) || "int".equals(clazz.toString()))				// Integer
			return Integer.valueOf(value).intValue();
		if (clazz.isAssignableFrom(Long.class) || "long".equals(clazz.toString()))				// Long
			return Long.valueOf(value).longValue();
		if (clazz.isAssignableFrom(Float.class) || "float".equals(clazz.toString()))				// Float
			return Float.valueOf(value);
		if (clazz.isAssignableFrom(Double.class) || "double".equals(clazz.toString()))		// Double
			return Double.valueOf(value).doubleValue();;
		if (clazz.isAssignableFrom(Boolean.class) || "boolean".equals(clazz.toString()))	// Boolean
			return Boolean.valueOf(value).booleanValue();
		if (clazz.isAssignableFrom(Date.class) && DateUtil.isDay(value))							// Date
			return DateUtil.parseDate(value);
		if (clazz.isAssignableFrom(Date.class) && DateUtil.isMinute(value))					// Time
			return DateUtil.parseTime(value);
		return value;
	}
	
	public static Object createObject(String className, Class superClass) throws CIBusException {
		try {
			Class clazz = Class.forName(className);
			if (superClass.isAssignableFrom(clazz)) {
				return clazz.newInstance();
			} else {
				throw new CIBusException("", "not support cause parent class");
			}
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
	}
	
	public static Object createObject(String className, ClassLoader loader) throws CIBusException {
		try {
			return loader.loadClass(className).newInstance();
		} catch (Exception e) {
			throw new CIBusException("", e);
		} 
	}
	
	public static Object deepClone(Object o, ClassLoader cl) throws CIBusException {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(bos);
			oos.writeObject(o);
			ois = new BusObjectInputStream(new ByteArrayInputStream(bos.toByteArray()), cl);
			return ois.readObject();
		} catch (Exception e) {
			DevAssistant.errorln(e);
			throw new CIBusException("" , e);
		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
				}
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
				}
		}
	}
	
	public static boolean hasArguments(Method m) {
		Class<?>[] as = m.getParameterTypes();
		if (as != null && as.length > 0)
			return true;
		return false;
	}
	
	public static Class loadClass(String className) throws CIBusException {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static Class loadClass(String className, ClassLoader loader) throws CIBusException {
		try {
			return Class.forName(className, false, loader);
		} catch (ClassNotFoundException e) {
			throw new CIBusException("", e);
		}
	}
	
	public static Object invokeStaticRet(String className, String function, Object[] args) throws CIBusException {
		Object ret = null;
		try {
			Class clazz = loadClass(className);
			Method method = getMethod(clazz, function, args);
			ret = method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		
		return ret;
	}
	
	public static Object invokeStaticRet(ClassLoader loader, String className, String function) throws CIBusException {
		Object ret = null;
		try {
			Class clazz = loadClass(className, loader);
			Method method = getMethod(clazz, function);
			ret = method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		
		return ret;
	}
	
	public static Object invokeStaticRet(String className, ClassLoader loader, String function, Object[] args) throws CIBusException {
		Object ret = null;
		try {
			Class clazz = loadClass(className, loader);
			Method method = getMethod(clazz, function, args);
			ret = method.invoke(null, null);
		} catch (Exception e) {
			throw new CIBusException("", e);
		}
		
		return ret;
	}
	
	public static String classpathForField(Field field) {
		return field.getDeclaringClass() + "." + field.getName();
	}
}
