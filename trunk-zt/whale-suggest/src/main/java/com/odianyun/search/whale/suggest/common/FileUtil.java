package com.odianyun.search.whale.suggest.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class FileUtil {
	static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static List<String> readTxtFile(String filename) throws Exception{
		InputStream input = FileUtil.class.getClassLoader()
				.getResourceAsStream(filename);
		BufferedReader br = new BufferedReader(new InputStreamReader(input,"utf-8"));
		List<String> result=new ArrayList<String>(); 
		String line="";
		try {
			while((line=br.readLine())!=null){
				line=line.replace("\r\n", "");
				line=line.replace("\n", "");
				result.add(line);
			}
		} catch (IOException e) {
			logger.error("read wordnet error! " + e);
		}finally{
			br.close();
		}
		return result;
	}
}
