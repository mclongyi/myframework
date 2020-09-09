package com.odianyun.search.whale.suggest.check;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.odianyun.search.whale.analysis.ik.IKSegment;
import com.odianyun.search.whale.suggest.model.QueryContext;
import com.odianyun.search.whale.suggest.model.TermWord;

public class QueryExtraction {

	private DomainLexcion lexcion; 
	protected IKSegment seg =new IKSegment(true);
	/**
	 * 预处理过程，分词、类型及权重；含有色情词的query不进行后面的处理；
	 * @param input
	 * @return 是否包含非法违禁词
	 */
	public boolean preprocess(String input,QueryContext wq) throws Exception{
		wq.setOriginal(input);
		List<String> tokens = seg.segment(input);
		if(CollectionUtils.isNotEmpty(tokens)){
			for(int i=0;i<tokens.size();i++){
				String token = tokens.get(i);
				int type = lexcion.getWordType(token);
				if(type==-1){
					return false;
				}else{
					TermWord tw =new TermWord(token,type);
					tw.setIndex(i);
					float score=lexcion.getWordEntropy(token);
					tw.setWeight(score);
					wq.addTerm(tw);
				}
			}
		}
		return true;
	}
}
