package com.longyi.csjl.frame.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 冒泡排序
 * 算法 比较两个相邻的元素 把大的放到右边 时间复杂度O(n^2)
 */
public class BubbleSort {

  public static void main(String[] args) {
      sort();
  }

  public static void sort(){
      int[] arr={10,3,1,34,5,4};
      for(int i=0;i<arr.length;i++){
          for(int j=0;j<arr.length-i-1;j++){
            if(arr[j]>arr[j+1]){
                int temp=arr[j];
                arr[j]=arr[j+1];
                arr[j+1]=temp;
            }
          }
      }
    System.out.println("排序后");
    System.out.println(arr.toString());
  }
}
