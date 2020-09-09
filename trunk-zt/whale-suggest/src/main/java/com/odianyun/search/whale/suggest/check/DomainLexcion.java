package com.odianyun.search.whale.suggest.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.odianyun.search.whale.suggest.common.FileUtil;


/**
 * 词典的信息熵
 * @author yuqian
 *
 */
public class DomainLexcion {

	static Logger logger = Logger.getLogger(DomainLexcion.class);
	Map<String,Float> entropyMap =new HashMap<String,Float>();
	Map<String,Integer> wordTypeMap = new HashMap<String,Integer>();
	
	public void init()throws Exception{
		List<String> lexList =FileUtil.readTxtFile("net_wordsV3.0.txt");
		for(String text:lexList){
			String[] data=text.split("#");
			entropyMap.put(data[0], Float.parseFloat(data[1])); 
		}
		List<String> wordlist = FileUtil.readTxtFile("words.txt");
		for(String text:wordlist){
			String[] array = text.split("/");
			wordTypeMap.put(array[0], Integer.parseInt(array[1]));
		}
	}
	
	public float getWordEntropy(String word){
		Float score = this.entropyMap.get(word);
		if(score!=null)
			return score.floatValue();
		else
			return 0.0f;
	}
	
	public int getWordType(String word){
		Integer type = this.wordTypeMap.get(word);
		if(type!=null)
			return type.intValue();
		else
			return 0;
	}
}
