package com.longyi.csjl.frame.algorithm;

import java.util.Arrays;

/**
 * 堆排序
 */
public class HeapSort {

  public static void main(String[] args) {

    int[] arr = new int[] {3, 5, 3, 0, 8, 6, 1, 5, 8, 6, 2, 4, 9, 4, 7, 0, 1, 8, 9, 7, 3, 1, 2, 5, 9, 7, 4, 0, 2, 6};
    HeapSort heapSort=new HeapSort(arr);
    heapSort.sort();
    System.out.println(Arrays.toString(arr));
  }

    private int[] arr;

    public HeapSort(int[] arr){
        this.arr=arr;
    }

    public void sort(){
        int len=arr.length-1;
        int beginIndex=(arr.length>>1)-1;
        for(int i=beginIndex;i>=0;i++){
            maxHeapify(i,len);
        }
        for(int i=len;i>0;i--){
            swap(0,i);
            maxHeapify(0,i-1);
        }
    }
    private void swap(int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private void maxHeapify(int index, int len) {
        int li = (index << 1) + 1; // 左子节点索引
        int ri = li + 1;           // 右子节点索引
        int cMax = li;             // 子节点值最大索引，默认左子节点。
        if (li > len) return;      // 左子节点索引超出计算范围，直接返回。
        if (ri <= len && arr[ri] > arr[li]) // 先判断左右子节点，哪个较大。
            cMax = ri;
        if (arr[cMax] > arr[index]) {
            swap(cMax, index);      // 如果父节点被子节点调换，
            maxHeapify(cMax, len);  // 则需要继续判断换下后的父节点是否符合堆的特性。
        }


    }
    }
