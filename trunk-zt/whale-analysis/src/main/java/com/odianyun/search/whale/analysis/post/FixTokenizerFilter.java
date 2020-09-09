package com.odianyun.search.whale.analysis.post;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TokenContext;

/**
 * 将英语与数字分开
 *
 * @author jing liu
 *
 */
public class FixTokenizerFilter implements PostTokenFilter {

	private Pattern needSplit = Pattern.compile("^([a-zA-Z]|[0-9])+$");
	private Pattern word = Pattern.compile("([a-zA-Z]+)");
	private Pattern number = Pattern.compile("([0-9]+)");

	private Pattern none = Pattern.compile("^(\\d|\\w|\\s|[\u4E00-\u9FA5])+$");

	@Override
	public List<String> action(TokenContext ctx, String token) {
		List<String> ret = new LinkedList<String>();

		if (ctx != null) {
			String raw = ctx.getRawInput();
			if (this.skip(raw)) {
				ret.add(token);
				return ret;
			}
		}

		Matcher m = this.needSplit.matcher(token);
		if (m.matches()) {
			Matcher w = this.word.matcher(token);
			while (w.find()) {
				ret.add(w.group(1));
			}

			Matcher n = number.matcher(token);
			while (n.find()) {
				ret.add(n.group(1));
			}
		} else {
			ret.add(token);
		}

		return ret;
	}

	private boolean skip(String text) {
		Matcher m = none.matcher(text);
		return m.matches();
	}
}
