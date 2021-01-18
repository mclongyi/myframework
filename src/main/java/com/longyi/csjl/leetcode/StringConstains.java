package com.longyi.csjl.leetcode;

/**
 * @author ly
 * @description 字符串包含的最优解
 * @date 2020/12/20 13:54
 * @throw
 */
public class StringConstains {
    public static void main(String[] args) {
        boolean b = StringContain("ABC", "ABD");
        System.out.println(b);
    }

    public static boolean StringContain(String str1,String str2){
        int hash=0;
        for(int i=0;i<str1.length();i++){
            hash|=(1<<(str1.toCharArray()[i]-'A'));
        }
        for(int i=0;i<str2.length();i++) {
            if((hash & (1<<(str2.toCharArray()[i]-'A'))) == 0){
                return false;
            }
        }
        return true;
    }

}    
   