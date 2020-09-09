package com.odianyun.search.whale.analysis.post;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;
import com.odianyun.search.backend.model.MainDict;
import com.odianyun.search.whale.analysis.PostTokenFilter;
import com.odianyun.search.whale.analysis.TokenContext;

/**
 * 同义词映射处理
 *
 * @author jing liu
 */
public class SynonymyFilter implements PostTokenFilter {
	
	static Logger log = Logger.getLogger(SynonymyFilter.class);

    //原始映射
    private Map<String, List<String>> map;

    /**
     * key是需要映射的token
     * value是映射的token list,用逗号分隔
     *
     * @param br 同义词词典
     */
    public SynonymyFilter(BufferedReader br) {
        String dict = "";
        this.map = new HashMap<String, List<String>>();
        try {
            while ((dict = br.readLine()) != null) {
                String[] dictArr = dict.split(",");
                List<String> values = new LinkedList<String>();
                Collections.addAll(values,dictArr);
                if (null != dict) {
                    for (String dictionary : dictArr) {
                        List<String> wordList = this.map.get(dictionary);
                        if (wordList == null) {
                            wordList = new LinkedList<String>();
                        }
                        wordList.addAll(values);
                        wordList.remove(dictionary);
                        map.put(dictionary, wordList);
                    }

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

        List<String> vals = this.map.get(token);
        if (vals != null) {
//            vals.remove(token);
            ret.addAll(vals);
        }
        return ret;
    }

}
