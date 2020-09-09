package com.odianyun.search.whale.analysis.post;

import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TokenContext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

/**
 * 同义词映射处理
 *
 * @author jing liu
 */
public class ExtendFilter implements PostTokenFilter {
	
	static Logger log = Logger.getLogger(ExtendFilter.class);

    //原始映射
    private Map<String, List<String>> values;

    /**
     * key是需要映射的token
     * value是映射的token list,用逗号分隔
     *
     * @param br 同义词词典
     */
    public ExtendFilter(BufferedReader br) {
        String dict = "";
        this.values = new HashMap<String, List<String>>();
        try {
            while ((dict = br.readLine()) != null) {
                String[] arr = dict.split("=");
                if (null != arr && arr.length == 2) {
                    String key = arr[0];
                    String value = arr[1];
                    List<String> wordList = values.get(key);
                    if (null == wordList) {
                        wordList = new LinkedList<String>();
                    }

                    String[] parts = value.split(",");
                    if (parts != null) {
                        for (String part : parts) {
                            if (part != null) {
                                part = part.trim();
                                if (!part.isEmpty()) {
                                    wordList.add(part);
                                }
                            }
                        }
                    }
                    this.values.put(key, wordList);
                }

            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
        	log.error(e.getMessage(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    //do nothing else
                	log.error(e.getMessage(), e);
                }
            }
        }

    }

    @Override
    public List<String> action(TokenContext ctx, String token) {
        List<String> ret = new LinkedList<String>();
        ret.add(token);

        List<String> vals = this.values.get(token);
        if (vals != null) {
            ret.addAll(vals);
        }
        return ret;
    }

}
