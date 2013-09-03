// org.apache.felix.framework.cache.URLContent.java
/**
 * Antelope CI平台，持续集成平台
 * 支持分布式部署测试，支持基于工程、任务多种集成模式
 * ------------------------------------------------------------------------
 * Copyright (c) 2013, Antelope CI Team All Rights Reserved.
*/

package org.apache.felix.framework.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.antelope.ci.bus.common.DebugUtil;


/**
 * 包含jar和url的content
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-8-26		下午4:14:02 
 */
public class JarURLContent implements Content {
	private static final int BUFSIZE = 4096;
	private URL url;
	private JarFile jarFile;
	
	public JarURLContent(URL url) throws Exception {
		this.url = url;
		jarFile = new JarFile(this.url.getFile());
	}

	@Override
	public void close() {
		try {
			jarFile.close();
		} catch (IOException e) {
		}
	}

	@Override
	public boolean hasEntry(String name) {
		return false;
	}	

	@Override
	public Enumeration<String> getEntries() {
		return null;
	}

	@Override
	public byte[] getEntryAsBytes(String name) {
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            ZipEntry ze = jarFile.getEntry(name);
            if (ze == null) {
                return null;
            }
            is = jarFile.getInputStream(ze);
            if (is == null) {
                return null;
            }
            DebugUtil.assert_out("ZipEntry = " + ze);
            DebugUtil.assert_out("find class = " + name);
            baos = new ByteArrayOutputStream(BUFSIZE);
            byte[] buf = new byte[BUFSIZE];
            int n = 0;
            while ((n = is.read(buf, 0, buf.length)) >= 0) {
                baos.write(buf, 0, n);
            }
            return baos.toByteArray();
        } catch (Exception ex)  {
            return null;
        } finally {
            try {
                if (baos != null) baos.close();
            } catch (Exception ex) {
            }
            try {
                if (is != null) is.close();
            } catch (Exception ex) {
            }
        }
	}

	@Override
	public InputStream getEntryAsStream(String name) throws IOException {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public Content getEntryAsContent(String name) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public String getEntryAsNativeLibrary(String name) {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public URL getEntryAsURL(String name) {
		return url;
	}

}

