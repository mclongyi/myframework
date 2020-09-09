package com.odianyun.search.whale.analysis;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.odianyun.search.whale.analysis.ik.IKSegment;

public class IKSegmentTest {
	
	public static void main(String[] args) throws Exception{
		 ISegment segment=new IKSegment(false);
		 List<String> ret=segment.segment("*");
		 System.out.println(ret);
	}

}
