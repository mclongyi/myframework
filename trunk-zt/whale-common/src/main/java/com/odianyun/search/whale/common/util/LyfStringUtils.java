package com.odianyun.search.whale.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hs
 * @date 2018/10/9.
 */
public class LyfStringUtils {

    /**
     * 判断是否是中文
     * @param text
     * @return true 中文
     */
    public static boolean isChinese(String text) {
        if (StringUtils.isEmpty(text)){
            return false;
        }
        String regex = "^[\u4E00-\u9FA5]+$";
        Matcher matcher = Pattern.compile(regex).matcher(text);
        return matcher.matches() && text.length() > 1;
    }


    /**
     * 保留汉字
     *
     * @param text 香辣藕片190g
     * @return 香辣藕片
     */
    public static String getChinese(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String regex = "([\u4e00-\u9fa5]+)";
        StringBuffer str = new StringBuffer();
        Matcher matcher = Pattern.compile(regex).matcher(text);
        while (matcher.find()) {
            str.append(matcher.group(0));
        }
        return str.toString();
    }

    /**
     * 去除数字和字母
     *
     * @param text 香辣藕片190g
     * @return 香辣藕片
     */
    public static String getTextNoAZ09(String text) {
        if (StringUtils.isEmpty(text)) {
            return "";
        }
        String regex = "[a-zA-Z0-9]";
        return text.replaceAll(regex, "");
    }


    /**
     * 过滤重复数据
     * @param keyword  数据格式: 1 2 1 2 4
     * @param split 打散格式: " "
     * @return 1 2 4
     */
    public static String filterRepeatedString(String keyword,String split){
        if (StringUtils.isNotEmpty(keyword) && !"".equals(split)) {
            String[] split1 = keyword.split(split);
            Set<String> ks = new HashSet<>(Arrays.asList(split1));
            StringBuffer sb = new StringBuffer();
            for (String k : ks) {
                sb.append(split).append(k);
            }
            return sb.toString().trim();
        }
        return keyword;
    }





    /**
     * 根据主词猜测该关键词是什么
     * 例如:
     * 量贩-巴旦木仁180gX2    巴旦木仁
     * 量贩-虎皮蛋糕168g(精装)X3  虎皮蛋糕
     * 马克西姆（树莓蓝莓夹心糖+什锦水果气泡糖）200g/组   马克西姆+树莓蓝莓夹心糖+什锦水果气泡糖
     *
     * @param keyword
     * @return
     * @throws Exception
     */
    public static String guessKeyword(String keyword) throws Exception {
        if (StringUtils.isEmpty(keyword)) {
            return "";
        }
        try {
            boolean startCheck = false;
            boolean endCheck = false;
            //移除前后空格,移除所有的数字和字母
            keyword = LyfStringUtils.getTextNoAZ09(keyword);
            String repl = "\\*";
            keyword = keyword.replaceAll(repl,"");
            keyword = keyword.replaceAll("亚米-","");
            keyword = keyword.replaceAll("亚米","");
            keyword = keyword.trim();

            //前置处理 start
            //1.以括号开头{} () [] ===> （门店寄售）美国鲜橙10斤装
            if (keyword.startsWith("(")) {
                int i = keyword.indexOf(")");
                keyword = keyword.substring(i + 1);
                keyword = keyword.trim();
                startCheck = true;
            }
            if (keyword.startsWith("{") && !startCheck) {
                int i = keyword.indexOf("}");
                keyword = keyword.substring(i + 1);
                keyword = keyword.trim();
                startCheck = true;
            }
            if (keyword.startsWith("[") && !startCheck) {
                int i = keyword.indexOf("]");
                keyword = keyword.substring(i + 1);
                keyword = keyword.trim();
                startCheck = true;
            }
            if (keyword.startsWith("【") && !startCheck) {
                int i = keyword.indexOf("】");
                keyword = keyword.substring(i + 1);
                keyword = keyword.trim();
                startCheck = true;
            }
            if (keyword.startsWith("（")) {
                int i = keyword.indexOf("）");
                keyword = keyword.substring(i + 1);
                keyword = keyword.trim();
                startCheck = true;
            }
            //2.日 月 年 关键词  -->2013年3-5月来伊份爱大自封袋
            if (!startCheck) {
                if (!keyword.contains("日本")) {
                    int i = keyword.indexOf("日");
                    if (i >= 0 && i != (keyword.length() - 1)) {
                        keyword = keyword.substring(i + 1);
                        keyword = keyword.trim();
                        startCheck = true;
                    }
                }
            }
            if (!startCheck) {
                int i = keyword.indexOf("月");
                if (i >= 0 && i != (keyword.length() - 1)) {
                    keyword = keyword.substring(i + 1);
                    keyword = keyword.trim();
                    startCheck = true;
                }
            }
            if (!startCheck) {
                int i = keyword.indexOf("年");
                if (i >= 0 && i != (keyword.length() - 1)) {
                    keyword = keyword.substring(i + 1);
                    keyword = keyword.trim();
                    startCheck = true;
                }
            }
            //3.第一个横线的位置  ->  量贩-虎皮蛋糕168g(精装)X3
            if (!startCheck) {
                int i = keyword.indexOf("-");
                if (i >= 0 && i != (keyword.length() - 1)) {
                    //横线前后是否有汉字
                    keyword = keyword.substring(i + 1);
                    keyword = keyword.trim();
                    startCheck = true;
                }
            }

            //后置处理 start
            //1.后面包含括号{} () [] ===> 伊仔自封袋（夏季版）--CD
            {
                int i = keyword.indexOf("(");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            if (!endCheck) {
                int i = keyword.indexOf("[");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            if (!endCheck) {
                int i = keyword.indexOf("{");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            {
                int i = keyword.indexOf("（");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            if (!endCheck) {
                int i = keyword.indexOf("【");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            //2.后置第一个横线的位置  ->  TQ-法式可口酥-乳酪250g
            if (!endCheck) {
                int i = keyword.indexOf("-");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
            //3. 空格  --> 9-抹茶冰淇淋C81 2kg
            if (!endCheck) {
                int i = keyword.indexOf(" ");
                if (i >= 0) {
                    keyword = keyword.substring(0, i);
                    keyword = keyword.trim();
                    endCheck = true;
                }
            }
        } catch (Exception e) {
            System.out.println("猜词出错," + keyword + "," + e);
            return "";
        }
        return keyword;
    }


    public static void main(String[] args) throws Exception {
        String keyword = "亚米-马来***西亚-香草味夹心巧克力味威化饼干****";
        String repl = "(\\*)[亚米]";
        keyword = keyword.replaceAll(repl,"");
//        keyword = keyword.replaceAll("亚米-","");
        System.out.println("k = "+keyword);

        String text = "亚米-AAAAdsda[来伊份]-香辣藕片190g&88000+=";
        System.out.println("原值：" + text);
        System.out.println("中文： " + getChinese(text));
        System.out.println("去除字母数字: " + getTextNoAZ09(text));
        System.out.println("猜词:"+guessKeyword(text));
        System.out.println();

        List<String> wordList = Arrays.asList(
//                "2013涂装公仔-Rock Star", "2018新店3-4月优惠劵 20减10",
//                "日本-日本三立源氏迷你奶油蝴蝶酥 (家庭装) 294g 28枚入",
//                "干脆范儿（鸡汁味)",
//                "量贩-鸡蛋煎饼（烘烤类糕点）120gX3",
//                "脆薯薯（牛排味）40g", "(门店专供不发货)阳澄之王牌大闸蟹688礼券", "180g香辣鸭锁骨(YK)SXZ",
//                "天天坚果()", "天天坚果 就是酷炫", "天天坚果-", "学院派-蟹香豆瓣165g",
//                "【量贩】天天坚果-温馨派25gX7", "万岁牌坚果蔬菜燕麦32g", "【组合】天天坚果全家福100g(4种各1小包）",
//                "【组合】坚果嘉年华套餐735g","潮香村美式芝士蛋糕实惠装60g*4（4种口味）",
                "亚米-马来西亚-香草味夹心巧克力味威化饼干"
        );
        for (String s : wordList) {
            String keywords = guessKeyword(s);
            System.out.println(keywords);
        }
    }


}
