package com.odianyun.search.whale.analysis.post;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import com.odianyun.search.whale.analysis.PinYin;
import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TokenContext;

public class PinyinFilter implements PostTokenFilter {
	
	static Logger log = Logger.getLogger(PinyinFilter.class);

	private static final Pattern ZH = Pattern.compile("^[\u4E00-\u9FA5]+$");

	@Override
	public List<String> action(TokenContext ctx, String token) {
		List<String> ret = new LinkedList<String>();
		ret.add(token);

		if (this.needTransform(token)) {
			try {
				ret.add(PinYin.toPinYin(token));
				ret.add(PinYin.toFirstSpell(token));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		return ret;
	}

	private boolean needTransform(String text) {
		Matcher m = ZH.matcher(text);
		return m.matches() && text.length() > 1;
	}
}
