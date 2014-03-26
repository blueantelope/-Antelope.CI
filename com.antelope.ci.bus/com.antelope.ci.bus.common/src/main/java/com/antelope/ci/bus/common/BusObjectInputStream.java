// com.antelope.ci.bus.common.BusObjectInputStream.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2014, Antelope CI Team All Rights Reserved.
 */

package com.antelope.ci.bus.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;

/**
 * 
 * @author blueantelope
 * @version 0.1
 * @Date 2014-3-25 下午5:36:02
 */
public class BusObjectInputStream extends ObjectInputStream {
	private static final HashMap<String, Class<?>> PRIM_CLASSES = new HashMap<String, Class<?>>(
			8, 1.0F);

	static {
		PRIM_CLASSES.put("boolean", boolean.class);
		PRIM_CLASSES.put("byte", byte.class);
		PRIM_CLASSES.put("char", char.class);
		PRIM_CLASSES.put("short", short.class);
		PRIM_CLASSES.put("int", int.class);
		PRIM_CLASSES.put("long", long.class);
		PRIM_CLASSES.put("float", float.class);
		PRIM_CLASSES.put("double", double.class);
		PRIM_CLASSES.put("void", void.class);
	}

	private ClassLoader cl;
	
	public BusObjectInputStream() throws IOException {
		super();
		this.cl = null;
	}

	public BusObjectInputStream(InputStream in) throws IOException {
		super(in);
		this.cl = null;
	}
	
	public BusObjectInputStream(ClassLoader cl) throws IOException {
		super();
		this.cl = cl;
	}

	public BusObjectInputStream(InputStream in, ClassLoader cl)
			throws IOException {
		super(in);
		this.cl = cl;
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		if (cl == null) {
			return super.resolveClass(desc);
		} else {
			String name = desc.getName();
			try {
				return Class.forName(name, false, cl);
			} catch (ClassNotFoundException ex) {
				Class<?> cl = PRIM_CLASSES.get(name);
				if (cl != null) {
					return cl;
				} else {
					throw ex;
				}
			}
		}
	}

}
