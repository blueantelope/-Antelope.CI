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

import com.antelope.ci.bus.common.DebugUtil;


/**
 * url content
 *
 * @author   blueantelope
 * @version  0.1
 * @Date	 2013-9-2		下午11:01:01 
 */
public class URLContent implements Content {
	private static final int BUFSIZE = 4096;
	private URL url;
	
	public URLContent(URL url) {
		this.url = url;
		
	}

	@Override
	public void close() {
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean hasEntry(String name) {
		
		// TODO Auto-generated method stub
		return false;
		
	}

	@Override
	public Enumeration<String> getEntries() {
		
		// TODO Auto-generated method stub
		return null;
		
	}

	@Override
	public byte[] getEntryAsBytes(String name) {
		if (url.toString().endsWith((name))) {
			DebugUtil.assert_out("find class url = " + url);
			InputStream in = null;
			ByteArrayOutputStream bos = null;
			try {
				bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[BUFSIZE];
				in = url.openStream();
				int len = -1;
				while ((len=in.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
					bos.flush();
				}
				return bos.toByteArray();
			} catch (Exception e) {
				return null;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
				if (bos != null) {
					try {
						bos.close();
					} catch (IOException e) {
					}
				}
			}
		}
		
		return null;
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
		
		// TODO Auto-generated method stub
		return null;
		
	}

}

