package com.longyi.csjl.datastructure.tree;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/25 21:59
 * @throw
 */
public class BSTTest {

    public static void main(String[] args) {
        BinarySearchTree<Integer> binarySearchTree=new BinarySearchTree<>();
        int[] nums={5,3,6,8,4,2};
        for(int i:nums){
            binarySearchTree.add(i);
        }
        binarySearchTree.preOrder();
        System.out.println("===========");
        binarySearchTree.inOrder();
        System.out.println("===========");
        binarySearchTree.afterOrder();
        System.out.println("============");
        binarySearchTree.preOrderNR();
        System.out.println("============");
        binarySearchTree.breadthOrder();
        System.out.println("===========");
        binarySearchTree.removeMin();
        binarySearchTree.removeMin();
        binarySearchTree.preOrder();
    }
}    
   