package com.odianyun.search.whale.analysis;

/**
 * 分词中介
 *
 * @author jing liu
 *
 */
public interface TokenContext {
	String getRawInput();
	String getInput();
	void put(String key, Object value);
	Object get(String key);
}
