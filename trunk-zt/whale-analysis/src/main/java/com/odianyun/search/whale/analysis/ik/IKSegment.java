package com.odianyun.search.whale.analysis.ik;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.odianyun.search.whale.analysis.Constants;
import com.odianyun.search.whale.analysis.ISegment;

/**
 * 使用过程中尽量保持单例模式
 * @author zengfenghua
 *
 */
public class IKSegment implements ISegment {

	Analyzer analyzer;

	String configPath;

	/**
	 * 用于兼容，使用Factory接口
	 */
	@Deprecated
	public IKSegment() {
		this(false);
	}

	/**
	 * 用于兼容，使用Factory接口
	 * @param use_smart
	 */
	@Deprecated
    public IKSegment(boolean use_smart) {
    	String global_path=Constants.getGlobalPath();
		Settings settings=ImmutableSettings.builder().put("use_smart", use_smart)
				.put(Constants.IK_HOME, global_path + Constants.SEARCH_RESOURCE).build();
	    Environment environment = new Environment(settings);
		//构建IK分词器，使用smart分词模式
	    analyzer = new IKAnalyzer(null,settings,environment);
	}

    /**
     * 外部构建，factory使用
     *
     * @param analyzer
     */
    public IKSegment(Analyzer analyzer) {
    	this.analyzer = analyzer;
    }


	@Override
	public List<String> segment(String text) throws Exception{
		List<String> ret=new ArrayList<String>();
		//获取Lucene的TokenStream对象
	    TokenStream ts = null;
		try {
			ts = analyzer.tokenStream("myfield", new StringReader(text));
		    //获取词元文本属性
		    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		    //重置TokenStream（重置StringReader）
			ts.reset();
			//迭代获取分词结果
			while (ts.incrementToken()) {
			    ret.add(term.toString());
			}
			//关闭TokenStream（关闭StringReader）
			ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
		} catch (IOException e) {
			throw e;
		} finally {
			//释放TokenStream的所有资源
			if(ts != null){
		      try {
				ts.close();
		      } catch (IOException e) {
				throw e;
		      }
			}
	    }
		return ret;
	}

}
