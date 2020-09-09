package com.odianyun.search.whale.resp.handler;
/**
 * Created by ody on 2016/8/8.
 * 词代码为进行编辑距离就算代码
 * 其中生成的词典（pinyinHanziDict.txt）代码为 data-service的 test 中的BrandServiceImplTest1.java代码(已经进行整理 现在为直接加载到代码中)
 */

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

//@Component
public class EditDistance
{
    static Logger logger = Logger.getLogger(EditDistance.class);
    @Autowired
     GenerativeDictionary getDic;
    private Map<Integer,Map<String,Map<String,String>>> pinyinChineseDic=new HashMap<>();
    private Map<String,String> pinyinChinese=new HashMap<>();
    static int preFixNum=3; //词典根据前面几个相同的 字符 存成一个list  然后循环匹配时只查找词list中的值，做编辑距离计算时，默认前三个词相同即不做计算
    int maxEditDistance=3; //进行编辑距离匹配时，如果两个拼音长度 大于此值时则不做计算
    int maxPreFix=3; //生成词典时，initPinyinDict（）中 传入的 prefix 必须小于此阈值，以后此函数prefix可以直接传入，根据需要处理


    public static int min(int a, int b, int c)
    {
        int t=a < b ? a : b;
        return t < c ? t : c;
    }
    /**
     *
     * @param strA 输入的字符串
     * @param strB 字典中的字符串
     * @return 返回编辑距离
     */
    public int editDistance(String strA, String strB)
    {
        char[] s1 = strA.substring(preFixNum).toLowerCase().toCharArray();
        char[] s2 = strB.substring(preFixNum).toLowerCase().toCharArray();
        int len1=s1.length;
        int len2=s2.length;
        int d[][]=new int[len1 + 1][len2 + 1];
        int i, j;
        for(i=0; i <= len1; i++)
            d[i][0]=i;
        for(j=0; j <= len2; j++)
            d[0][j]=j;
        for(i=1; i <= len1; i++)
            for(j=1; j <= len2; j++)
            {
                int cost=s1[i - 1] == s2[j - 1] ? 0 : 1;
                int deletion=d[i - 1][j] + 1;
                int insertion=d[i][j - 1] + 1;
                int substitution=d[i - 1][j - 1] + cost;
                d[i][j]=min(deletion, insertion, substitution);
            }
        return d[len1][len2];
    }
    /**进行判断
    1.上一步计算出传入的字符串与我们词典中的字符串进行编辑距离计算；
    2.下面进行判断，如果minEdit大于我们上步计算的距离，则最小编辑距离改变，对应的machEdit也改变
    3.如果得到编辑距离一样看得到的词出现的概率是否大于我们已得到的概率 如果满足则改变machEdit
     * @param strA 传入要匹配的词
     * @param pinyinChineseFreqMap 初始化得到的词典表
     * @return 返回结果为 PinyinHanziDict类型 里面包含四个字段 分别为拼音；汉字；词频；编辑距离（默认为0）
     */

    public String editDistance(String strA,int companyId){
        int minEditDistance=maxEditDistance+1;
        String minMachEditChinese="";
        String strAPreFixName = strA.substring(0, preFixNum);
        pinyinChineseDic=getDic.getPinyinChineseDic();
        Map<String,String> pinyinChinese=pinyinChineseDic.get(companyId).get(strAPreFixName);
        if(pinyinChinese!=null && !pinyinChinese.isEmpty()){
            for (Map.Entry<String, String> cc : pinyinChinese.entrySet()) {
                String pinyin = cc.getKey();
                if (Math.abs(strA.length() - pinyin.length()) <= maxEditDistance) {
                    int editDistance = editDistance(strA, pinyin);
                    if (minEditDistance > editDistance) {
                        minMachEditChinese = cc.getValue();
                        minEditDistance = editDistance;
                    }
                    }
            }
            return minMachEditChinese;
        }
        return null;
    }

    public  String getChineseByPinyin(String strA,int companyId){
        if(!pinyinChineseDic.containsKey(companyId))
            return null;

        for (Map.Entry<String,Map<String,String>> c:pinyinChineseDic.get(companyId).entrySet()) {
            String prefixName = c.getKey();
            int end = strA.length();
            if(end > preFixNum){
                end = preFixNum;
            }
            String strAPreFixName = strA.substring(0, end);
            if (strAPreFixName.equals(prefixName)) {
                for (Map.Entry<String, String> cc : c.getValue().entrySet()) {
                    String pinyin = cc.getKey();
                    if (Math.abs(strA.trim().length() - pinyin.trim().length()) == 0) {
                        if (strA.trim().toLowerCase().equals(pinyin.trim().toLowerCase())) {
                            return cc.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

}
