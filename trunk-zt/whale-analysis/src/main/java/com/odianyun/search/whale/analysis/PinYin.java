package com.odianyun.search.whale.analysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.odianyun.search.whale.common.util.LyfStringUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * 
 * @author yuqian
 *
 */
public class PinYin {

	static Logger log = Logger.getLogger(PinYin.class);

	public static String toPinYin(String text)throws Exception{
		//设置拼音格式
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//转换
		StringBuilder builder=new StringBuilder();//存放拼音
		for(char c:text.toCharArray()){
			if(Latin.isCJKCharacter(c)){
				String[] array=PinyinHelper.toHanyuPinyinStringArray(c, format);
				builder.append(array[0]);//对于多音字，返回第一个拼音
			}else{
				//去除空白区别
				if(!Character.isSpaceChar(c)){
					builder.append(c);
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * 汉字转拼音首字母缩写
	 * @param text
	 * @return
	 */
	public static String toFirstSpell(String text)throws Exception{
		//设置拼音格式
		HanyuPinyinOutputFormat format=new HanyuPinyinOutputFormat();
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		//转换
		StringBuilder builder=new StringBuilder();//存放拼音
		for(char c:text.toCharArray()){//遍历每个汉字
			if(Latin.isCJKCharacter(c)){
				String[] array=PinyinHelper.toHanyuPinyinStringArray(c, format);
				builder.append(array[0].charAt(0));//对于多音字，返回第一个拼音的首字母
			}else{
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	public static String toSort (String text){
		char[] arrays= text.toCharArray();
		Arrays.sort(arrays);
		return new String(arrays);
	}
	
	public static List<String> cutOneChar(String text){
		List<String> ret = new ArrayList<String>();
		int len = text.trim().length();
		String s;
		for(int i=0;i<len;i++){
			if(i==0){
				s = text.substring(1); 
			}else if(i==len-1){
				s = text.substring(0, len-1);
			}else{
				s = text.substring(0,i)+text.substring(i+1);
			}
			ret.add(s);
		}
		return ret;
	}


	/**
	 * 获取中文的拼音
	 * @param token   坚果
	 * @param pinyin true 全拼音
	 * @param firstSpell  true 首字母拼音
	 * @return jianguo jg
	 */
	public static List<String> transZHToPinyin(String token,Boolean pinyin,Boolean firstSpell) {
		List<String> ret = new LinkedList<String>();
		if (LyfStringUtils.isChinese(token)) {
			try {
				if (pinyin){
					ret.add(PinYin.toPinYin(token));
				}
				if (firstSpell){
					ret.add(PinYin.toFirstSpell(token));
				}
			} catch (Exception e) {
				log.error("汉字转拼音失败["+token+"] :", e);
			}
		}
		return ret;
	}

	public static void main(String[] args){
		List<String> 坚果 = transZHToPinyin("坚果", true, true);
		List<String> list =Arrays.asList(
				"1","2","3",
				"4","5","6"
		);
		System.out.println(list.subList(0,3));
		System.out.println(list);
	}
}
