package com.longyi.csjl.leetcode;

/**
 * @author ly
 * @description 字符串转整数
 * 比如 “123” -->从左往右遍历 1 *10第二次 1*10+2 第三次 12*10+3=123
 * @date 2020/12/20 14:46
 * @throw
 */
public class StringToInt {

    public static void main(String[] args) {
        String str="124";
        int sum = strToInt("124");
        System.out.println(sum);
    }

    public static int strToInt(String str){
        int sign=1;
        char[] chars = str.toCharArray();
        int sum=0;
        int i=0;
        while (i<str.length()){
            if(chars[0] == '-'){
                sign=-1;
            }else{
              int c=chars[i]-'0';
              sum=sum*10+c;
            }
            i++;
        }
        return sum*sign;
    }
}    
   