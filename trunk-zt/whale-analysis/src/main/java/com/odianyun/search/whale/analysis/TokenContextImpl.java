package com.odianyun.search.whale.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jing.liu
 *
 */
public class TokenContextImpl implements TokenContext {

	private String raw;
	private String filter;
	private Map<String, Object> objs = new HashMap<String, Object>();

	public void setRawInput(String raw) {
		this.raw = raw;
	}

	public void setInput(String input) {
		this.filter = input;
	}

	@Override
	public String getRawInput() {
		return this.raw;
	}

	@Override
	public String getInput() {
		return this.filter;
	}

	@Override
	public void put(String key, Object value) {
		this.objs.put(key, value);
	}

	@Override
	public Object get(String key) {
		return this.objs.get(key);
	}

}
