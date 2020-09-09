package com.odianyun.search.whale.analysis.user;

import java.util.LinkedList;
import java.util.List;

import com.odianyun.search.whale.analysis.ISegment;
import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.SegmentPolicy;
import com.odianyun.search.whale.analysis.TextFilter;
import com.odianyun.search.whale.analysis.ik.AutoReloadableISegmentFactory;
import com.odianyun.search.whale.analysis.ik.IKSegment;
import com.odianyun.search.whale.analysis.ik.ReloadableISegmentFactory;
import com.odianyun.search.whale.analysis.ik.TextFilterSegment;
import com.odianyun.search.whale.analysis.ik.TokenContextSegment;
import com.odianyun.search.whale.analysis.post.FixTokenizerFilter;
import com.odianyun.search.whale.analysis.post.PostTokenFilterSegment;
import com.odianyun.search.whale.analysis.post.SimplifiedChineseFilter;
import com.odianyun.search.whale.analysis.post.SymbolTextFilter;

/**
 * 构建搜索分词链
 *
 * @author jing liu
 *
 */
public class SearchPolicy implements SegmentPolicy {

	private ReloadableISegmentFactory factory;

	public SearchPolicy() throws Exception {
		factory = new AutoReloadableISegmentFactory();
	}

	@Override
	public ISegment get() throws Exception {
		//创建底层分词器
		ISegment smart = this.factory.create(true);
		//创建过滤器
		List<PostTokenFilter> filters = new LinkedList<PostTokenFilter>();
		FixTokenizerFilter fix = new FixTokenizerFilter();
		filters.add(fix);
		SimplifiedChineseFilter chinese = new SimplifiedChineseFilter();
		SymbolTextFilter symbol = new SymbolTextFilter();
        filters.add(symbol);
		//filters.add(chinese);
		PostTokenFilterSegment top = new PostTokenFilterSegment(smart, filters);

		List<TextFilter> pres = new LinkedList<TextFilter>();
		pres.add(chinese);
//		pres.add(symbol);

		TextFilterSegment ret = new TextFilterSegment(top, pres);


		//构建完整分词策略
		return new TokenContextSegment(ret);
	}

	@Override
	public ISegment get(boolean useSmart) throws Exception {
		//创建底层分词器
		ISegment smart = this.factory.create(useSmart);
		//创建过滤器
		List<PostTokenFilter> filters = new LinkedList<PostTokenFilter>();
		FixTokenizerFilter fix = new FixTokenizerFilter();
		filters.add(fix);
		SimplifiedChineseFilter chinese = new SimplifiedChineseFilter();
		SymbolTextFilter symbol = new SymbolTextFilter();
		filters.add(symbol);
		//filters.add(chinese);
		PostTokenFilterSegment top = new PostTokenFilterSegment(smart, filters);

		List<TextFilter> pres = new LinkedList<TextFilter>();
		pres.add(chinese);
//		pres.add(symbol);

		TextFilterSegment ret = new TextFilterSegment(top, pres);


		//构建完整分词策略
		return new TokenContextSegment(ret);
	}

	@Override
	public void reload() throws Exception {
		this.factory.reload();
	}
	
	public static void main(String[] args) throws Exception{
		SearchPolicy searchPolicy=new SearchPolicy();
		ISegment iSegment=searchPolicy.get();
		System.out.println(iSegment.segment("20151200079LY4V9"));
		
		IKSegment ikSegment=new IKSegment(true);
		System.out.println(ikSegment.segment("20151200079ly4v9"));
	}
}
