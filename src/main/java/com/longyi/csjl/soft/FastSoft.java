package com.longyi.csjl.soft;

import java.util.Arrays;

/**
 * @author ly
 * @description 快速排序 在分治法的思想下，原数列在每一轮被拆分成两部分，每一部分在下一轮又分别被拆分成两部分，直到不可再分为止。
 * 时间复杂度 复杂度是 O（nlogn），最坏情况下的时间复杂度是 O（n^2）。
 * @date 2020/12/16 16:06
 * @throw
 */
public class FastSoft {

    public static void main(String[] args) {
        int[] num = new int[]{6,1,2,7,9,3};
        quickSort(num, 0, num.length-1);
        System.out.println(Arrays.toString(num));
    }

    public static int[] quickSort(int[] num, int leftPos, int rightPos) {
        if(rightPos < leftPos)
            return num;
        else {
            //将数列最左边第一个数字作为基准数
            int initLeftPos = leftPos;
            int initRightPos = rightPos;
            int baseNum = num[leftPos];

            while(rightPos > leftPos) {
                //第二步：右边指针找到小于基准数的就停下
                while(num[rightPos] >= baseNum & rightPos > leftPos) {
                    rightPos--;
                }

                //第二步：左边指针找到大于基准数的就停下
                while(num[leftPos] <= baseNum & rightPos > leftPos) {
                    leftPos++;
                }

                //交换两个指针最终标记的数字
                if(rightPos > leftPos)
                    swap(num,leftPos,rightPos);
            }

            //当左右两边指针重合时，将基准数与指针指向数字交换
            swap(num,leftPos,initLeftPos);

            //指针左半边递归，以进来的数组的左边为界，右边是左右指针相同时左边一个
            quickSort(num, initLeftPos, leftPos-1);

            //右边同理
            quickSort(num, rightPos+1, initRightPos);

            return num;
        }
    }

    //swap方法：将数组中leftPos和rightPos上的两个数值进行交换
    public static void swap(int[] num,int leftPos,int rightPos) {
        int temp = num[leftPos];
        num[leftPos] = num[rightPos];
        num[rightPos] = temp;
    }
}    
   