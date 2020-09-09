package com.odianyun.search.whale.analysis;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * 子类资源先加载
 *
 * @author jing liu
 *
 */
public class ChildFirstClassLoader extends URLClassLoader {

	public ChildFirstClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		try {
			Class<?> c = super.findClass(name);
			return c;
		} catch (ClassNotFoundException e) {
			return super.loadClass(name);
		}
	}
}
