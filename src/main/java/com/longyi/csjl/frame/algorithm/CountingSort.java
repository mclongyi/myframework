package com.longyi.csjl.frame.algorithm;

/**
 * 计数排序算法
 * 时间复杂度O(N)
 */
public class CountingSort {

  public static void main(String[] args) {
    int[] arr={0,2,5,3,7,9,10,3,7,6};
    int[] result = countSort(arr);
    System.out.println("排序后:"+arr);
  }

    /**
     * 计数排序算法
     * @param A
     * @return
     */
    public static int[] countSort(int[] A){
        int max=Integer.MIN_VALUE;
         for(int num:A){
            max=Math.max(max,num);
        }
        //初始化数组容量
        int[] count=new int[max+1];
        for(int num:A){
            count[num]++;
        }

        int[] result=new int[A.length];
        int index=0;
        for (int i=0;i<count.length;i++){
            while (count[i]>0){
                result[index++]=i;
                count[i]--;
            }
        }
        return result;
    }

    public static int[] sort(int[] A){
        int min=Integer.MAX_VALUE;
        int max=Integer.MIN_VALUE;
        for(int num:A){
            max=Math.max(max,num);
            min=Math.min(min,num);
        }
        int[] count=new int[max-min-1];
        for(int num:A){
            count[num-min]++;
        }
        for (int i=1; i<count.length; i++) {
            count[i] += count[i-1];
        }
        // 创建结果数组
        int[] result = new int[A.length];
        // 遍历A中的元素，填充到结果数组中去
        for (int j=0; j<A.length; j++) {
            result[count[A[j]-min]-1] = A[j];
            count[A[j]-min]--;
        }
        return result;
    }
}
