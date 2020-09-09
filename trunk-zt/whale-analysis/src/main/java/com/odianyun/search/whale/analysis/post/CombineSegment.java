package com.odianyun.search.whale.analysis.post;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.odianyun.search.whale.analysis.ISegment;

/**
 * 聚合多个分词器
 *
 * @author jing liu
 *
 */
public class CombineSegment implements ISegment {

	private List<ISegment> all;

	public CombineSegment(List<ISegment> all) {
		this.all = all;
	}

	public void add(ISegment seg) {
		this.all.add(seg);
	}

	@Override
	public List<String> segment(String text) throws Exception {
		Set<String> ret = new HashSet<String>();

		for (ISegment s : this.all) {
			ret.addAll(s.segment(text));
		}

		return new LinkedList<String>(ret);
	}

}
