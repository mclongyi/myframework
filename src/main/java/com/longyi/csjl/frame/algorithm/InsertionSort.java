package com.longyi.csjl.frame.algorithm;

/**
 * 插入排序
 * 算法：每次把后面一个和前面一个进行对比 如果后面比前面大就进行交换
 * 时间复杂度是 O(N^2)
 */
public class InsertionSort {

  public static void main(String[] args) {
      soft();
  }

  public static void soft(){
      int[] arr={10,3,5,50,2,1,23};
      for (int i=1;i<arr.length;i++){
          int j=i;
          while (j>0){
              if(arr[j]<arr[j-1]){
                  int temp=arr[j];
                  arr[j]=arr[j-1];
                  arr[j-1]=temp;
                  j--;
              }else{
                  break;
              }
          }
      }
    System.out.println("排序后:");
    for (int i : arr) {
      System.out.println(i);
    }
  }

}
