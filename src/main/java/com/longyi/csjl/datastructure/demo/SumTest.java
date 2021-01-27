package com.longyi.csjl.datastructure.demo;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/24 17:12
 * @throw
 */
public class SumTest {

    public static void main(String[] args) {
        int[] arr={1};
        System.out.println(sum((arr))) ;
    }


    public static int sum(int[] arr){
            return sum(arr,0);
    }

    public static int sum(int[] arr,int index){
        if(index == arr.length){
            return 0;
        }
        return arr[index]+ sum(arr,index+1);
    }
}    
   