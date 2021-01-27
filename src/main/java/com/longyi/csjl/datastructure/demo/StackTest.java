package com.longyi.csjl.datastructure.demo;

import com.longyi.csjl.datastructure.ArrayStack;
import org.springframework.util.StringUtils;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/21 21:23
 * @throw
 */
public class StackTest {

    public static void main(String[] args) {
        boolean check = isCheck("[}[}");
        System.out.println(check);
        boolean check1 = isCheck("{[()]}");
        System.out.println(check1);
    }

    /**
     * 有效括号问题 {([])}
     * @param str
     * @return
     */
    public static boolean isCheck(String str){
           ArrayStack<Character> stack=new ArrayStack<>();
           for(int i=0;i<str.length();i++){
               char c = str.charAt(i);
               if(c =='(' || c== '{' || c=='['){
                    stack.push(c);
               }else{
                   if(stack.isEmpty()){
                       return false;
                   }
                   Character top = stack.pop();
                   if(c ==')' && top!='('){
                       return false;
                   }
                   if(c =='}' && top!='{'){
                       return false;
                   }
                   if(c ==']' && top!='['){
                       return false;
                   }
               }
           }
        return stack.isEmpty();
       }

}    
   