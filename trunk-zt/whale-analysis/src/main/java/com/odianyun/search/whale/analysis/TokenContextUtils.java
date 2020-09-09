package com.odianyun.search.whale.analysis;

public class TokenContextUtils {
	private static final ThreadLocal<TokenContext> local = new ThreadLocal<TokenContext>();

	public static void set(TokenContext ctx) {
		local.set(ctx);
	}

	public static TokenContext get() {
		return local.get();
	}

	public static void clear() {
		local.set(null);
	}
}
