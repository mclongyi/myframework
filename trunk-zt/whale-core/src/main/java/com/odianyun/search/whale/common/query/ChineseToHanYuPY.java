package com.odianyun.search.whale.common.query;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汉字转拼音，能处理多音字
 * 
 * @author phoebe
 * 
 */

public class ChineseToHanYuPY {
	static Logger logger = Logger.getLogger(ChineseToHanYuPY.class);
	public static final ChineseToHanYuPY instance=new ChineseToHanYuPY();
	private  Map<String, List<String>> pinyinMap = new HashMap<String, List<String>>();
	private ChineseToHanYuPY(){ initPinyin("duoyinzi_dic.txt"); }
	/**
	 * 将某个字符串的首字母 大写
	 * @param str
	 * @return
	 */
	public  String convertInitialToUpperCase(String str){
//		initPinyin("duoyinzi_dic.txt");
		if(str==null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		char[] arr = str.toCharArray();
		for(int i=0;i<arr.length;i++){
			char ch = arr[i];
			if(i==0){
				sb.append(String.valueOf(ch).toUpperCase());
			}else{
				sb.append(ch);
			}
		}
		
		return sb.toString();
	}

	/**
	 * 汉字转拼音 最大匹配优先
	 * @param chinese
	 * @return
	 */
	public  String convertChineseToPinyin(String chinese) {

		StringBuffer pinyin = new StringBuffer();
 		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		char[] arr = chinese.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char ch = arr[i];
			if (ch > 128) { // 非ASCII码
				// 取得当前汉字的所有全拼
				try {
					String[] results = PinyinHelper.toHanyuPinyinStringArray(
							ch, defaultFormat);
					if (results == null) {	//非中文
						return "";
					} else {
						int len = results.length;
						int xlen=0;
						if (len == 1) { // 不是多音字
							String py = results[0];		
							if(py.contains("u:")){	//过滤 u:
								py = py.replace("u:", "v");
							}
							pinyin.append(py);	//如果不需要拼音首字母大写 ，直接返回即可
//							pinyin.append(convertInitialToUpperCase(py));
						}else if(results[0].equals(results[1])){	//非多音字 有多个音，取第一个
							pinyin.append(results[0]);
//							pinyin.append(convertInitialToUpperCase(results[0]));
						}else { // 多音字
							int length = chinese.length();
							boolean flag = false;
							String s = null;
							List<String> keyList =null;
							for (int x = 0; x < len; x++) {
								String py = results[x];
								if(py.contains("u:")){	//过滤 u:
									py = py.replace("u:", "v");
								}
								keyList = pinyinMap.get(py);
								
								if (i + 3 <= length) {	//后向匹配2个汉字  大西洋 
									s = chinese.substring(i, i + 3);
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);
//										pinyin.append(convertInitialToUpperCase(py));
										flag = true;
										break;
									}

								}
								
								if (i + 2 <= length) {	//后向匹配 1个汉字  大西
									s = chinese.substring(i, i + 2);
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);
//										pinyin.append(convertInitialToUpperCase(py));
										flag = true;
										break;
									}
								}
								
								if ((i - 2 >= 0) && (i+1<=length)) {	// 前向匹配2个汉字 龙固大
									s = chinese.substring(i - 2, i+1);
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);
//										pinyin.append(convertInitialToUpperCase(py));
										flag = true;
										break;
									}
								}
								
								if ((i - 1 >= 0) && (i+1<=length)) {	// 前向匹配1个汉字   固大
									s = chinese.substring(i - 1, i+1);
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);
//										pinyin.append(convertInitialToUpperCase(py));
										flag = true;
										break;
									}
								}
								
								if ((i - 1 >= 0) && (i+2<=length)) {	//前向1个，后向1个      固大西
									s = chinese.substring(i - 1, i+2);
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);
//										pinyin.append(convertInitialToUpperCase(py));
										flag = true;
										break;
									}
								}

								if (x==(len-1) && (i+1<=length)){ //这是后续添加 记得检测准确性
									pinyin.append(results[0]);
//									pinyin.append(convertInitialToUpperCase(results[0]));
									flag = true;
									break;
								}
							}
							
							if (!flag) {	//都没有找到，匹配默认的 读音  大 
								
								s = String.valueOf(ch);
								
								for (int x = 0; x < len; x++) {
									
									String py = results[x];
									
									if(py.contains("u:")){	//过滤 u:
										py = py.replace("u:", "v");
									}
									
									keyList = pinyinMap.get(py);
									
									if (keyList != null && (keyList.contains(s))) {
										pinyin.append(py);	//如果不需要拼音首字母大写 ，直接返回即可
//										pinyin.append(convertInitialToUpperCase(py));//拼音首字母 大写
										break;
									}
								}
							}

						}
					}

				} catch (BadHanyuPinyinOutputFormatCombination e) {
					logger.error(e.getMessage(), e);
				}
			} else {
				pinyin.append(arr[i]);
			}
		}
		return pinyin.toString();
	}

	/**
	 * 初始化 所有的多音字词组
	 * 
	 * @param fileName
	 */
	private  void initPinyin(String fileName)  {
		// 读取多音字的全部拼音表;
		String filePath=ChineseToHanYuPY.class.getClassLoader().getResource(fileName).getFile();
		Map<String, List<String>> pinyinMap = new HashMap<>();
		BufferedReader br = null;
		String s = null;
		try {
			filePath = URLDecoder.decode(filePath,"utf-8");
			InputStreamReader file = new InputStreamReader(new FileInputStream(new File(filePath)), "utf-8");
			br = new BufferedReader(file);
//			BufferedReader br = new BufferedReader(new InputStreamReader(file));
			while ((s = br.readLine()) != null) {

				if (s != null) {
					String[] arr = s.split("#");
					String pinyin = arr[0];
					String chinese = arr[1];

					if(chinese!=null){
						String[] strs = chinese.split(" ");
						List<String> list = Arrays.asList(strs);
						pinyinMap.put(pinyin, list);
					}
				}
			}
		this.pinyinMap = pinyinMap;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
