package com.longyi.csjl.leetcode;

/**
 * @author ly
 * @description 字符串反转
 * 例如，字符串 abcdef ，若要让def翻转到abc的前头，只要按照下述3个步骤操作即可：
 * 首先将原字符串分为两个部分，即X:abc，Y:def；
 * 将X反转，X->X^T，即得：abc->cba；将Y反转，Y->Y^T，即得：def->fed。
 * 反转上述步骤得到的结果字符串X^TY^T，即反转字符串cbafed的两部分（cba和fed）给予反转，
 * cbafed得到defabc，形式化表示为(X^TY^T)^T=YX，这就实现了整个反转。
 * @throw
 */
public class StringReserve {

    public static void main(String[] args) {
//        String str="abcdef";
//        str= LeftRotateString(str.toCharArray(), str.length(), 3);
//        System.out.println("反转之后的:"+ str);

//        String str="Ilovebaofeng";  //反转后的 baofengIlove
//        str= LeftRotateString(str.toCharArray(), str.length(), 5);
//        System.out.println("反转之后的:"+ str);
          String str="I am a student.";
        str= test(str.toCharArray(), str.length(), 7);
        System.out.println(str);
    }

    public static String test(char[] s,int n,int m){
        reserveStr(s,m,n-1);
        reserveStr(s,0,n-1);

        return new String(s);
    }


    public static void reserveStr(char[] arr,int from,int to){
        while (from<to){
            char t=arr[from];
            arr[from++]=arr[to];
            arr[to--]=t;
        }
    }

    public static String LeftRotateString(char[] s,int n,int m)
    {
        m %= n;               //若要左移动大于n位，那么和%n 是等价的
        reserveStr(s, 0, m - 1); //反转[0..m - 1]，套用到上面举的例子中，就是X->X^T，即 abc->cba
        reserveStr(s, m, n - 1); //反转[m..n - 1]，例如Y->Y^T，即 def->fed
        reserveStr(s, 0, n - 1); //反转[0..n - 1]，即如整个反转，(X^TY^T)^T=YX，即 cbafed->defabc。
        return new String(s);
    }


}    
   