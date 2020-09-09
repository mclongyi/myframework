package com.odianyun.search.whale.analysis.post;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TextFilter;
import com.odianyun.search.whale.analysis.TokenContext;

/**
 * Created by fishcus on 16/10/31.
 */
public class SymbolTextFilter implements TextFilter,PostTokenFilter {

    String regEx = "[\\-\\_#&]";

    public SymbolTextFilter(){
    }

    @Override
    public String filter(TokenContext ctx, String text) {
        return   StringFilter(text);
    }

    public String StringFilter(String str){
        // 清除掉所有特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

	@Override
	public List<String> action(TokenContext ctx, String token) {
        List<String> ret = new LinkedList<String>();
        if(token.endsWith("-")||token.endsWith("_")){
        		token = token.substring(0, token.length()-1);
        }
        ret.add(token);
		return ret;
	}
}
