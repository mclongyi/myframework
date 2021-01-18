package com.longyi.csjl.soft;

import java.util.Arrays;

/**
 * @author ly
 * @description ：选择排序，从头至尾扫描序列，找出最小的一个元素，
 *                            和第一个元素交换，接着从剩下的元素中继续这种选择和交换方式，最终得到一个有序序列。
 * 时间复杂度
 * @date 2020/12/16 14:59
 * @throw
 */
public class SelectSoft {

    public static void main(String[] args) {
        int[] arr={30,3,1,3,4,90,8,7,12};
        selectSoft(arr,arr.length);
    }

    public static void selectSoft(int[] arr,int n){
        for(int i=0;i<n-1;i++){
            int index=i;
            int j;
            for(j=i+1;j<n;j++){
                if(arr[j]<arr[index]){
                    index=j;
                }
            }
            int tmp = arr[index];
            arr[index] = arr[i];
            arr[i] = tmp;
            System.out.println(Arrays.toString(arr));
        }
    }

}    
   