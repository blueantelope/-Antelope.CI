// com.antelope.ci.bus.JarFileReader.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package com.antelope.ci.bus.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;


/**
 * jar包中class读取器
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-22		下午11:10:52 
 */
public class JarClassReader {
	private JarFile jarFile;
	private JarInputStream jarInput;  
    private HashMap<String, ByteArrayOutputStream> classStreamMap;  
    
    public JarClassReader(String jarPath) throws IOException {  
    		jarFile = new JarFile(jarPath);
        jarInput = new JarInputStream(new FileInputStream(jarPath));  
        classStreamMap = new HashMap<String, ByteArrayOutputStream>();
        readClassEntries();
    }  
      
    private void readClassEntries() throws IOException {  
    		Enumeration e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry entry = (JarEntry) e.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith(".class")){
                continue;
            }
            String className = entry.getName().substring(0, entry.getName().length()-6);
            className = className.replace('/', '.');
            addClassInputStream(jarInput, className);     
        }
    }  
      
    private void addClassInputStream(InputStream in, String className) throws IOException {  
        if(!classStreamMap.containsKey(className)) {  
            ByteArrayOutputStream _copy = new ByteArrayOutputStream();  
            int read = 0;  
            int chunk = 0;  
            byte[] data = new byte[256];  
            while((chunk = in.read(data)) != -1)   {  
                read += data.length;  
                _copy.write(data, 0, chunk);  
            }  
            classStreamMap.put(className, _copy);  
        }  
    }  
    
    /**
     * 取得jar中每个class的输入流
     * @param  @param className
     * @param  @return
     * @return InputStream
     * @throws
     */
    public InputStream getClassStream(String className) {  
        ByteArrayOutputStream _copy = classStreamMap.get(className);  
        return (InputStream)new ByteArrayInputStream(_copy.toByteArray());  
    }  
    
    /**
     * 取得jar中每个class的字节数组
     * @param  @param className
     * @param  @return
     * @return byte[]
     * @throws
     */
    public byte[] getClassBytes(String className) {
    		ByteArrayOutputStream _copy = classStreamMap.get(className);
    		return _copy.toByteArray();
    }
}

