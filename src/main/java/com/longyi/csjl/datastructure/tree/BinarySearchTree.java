package com.longyi.csjl.datastructure.tree;

/**
 * @author ly
 * @description 二分搜索树
 * @date 2021/1/25 20:04
 * @throw
 */
public class BinarySearchTree<E extends Comparable<E>> {
    private class Node{
        public E e;
        private Node left,right;

        public Node(E e){
            this.e=e;
            left=null;
            right=null;
        }
    }

    private Node root;
    private int size;

    public BinarySearchTree(){
        root=null;
        size=0;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size==0;
    }

    /**
     * 添加树节点
     * @param e
     */
    public void add(E e){
           root=add(root,e);
    }
        private Node add(Node node, E e){
        if(node ==null){
            size++;
            return new Node(e);
        }
        if(e.compareTo(node.e)<0){
            node.left= add(node.left,e);
        }else if(e.compareTo(node.e)>0){
           node.right= add(node.right,e);
        }
        return node;
    }

    /**
     * 查询是否包容元素e
     * @param e
     * @return
     */
    public boolean contains(E e){
        return contains(root,e);
    }

    private boolean contains(Node root,E e){
        if(root == null){
            return false;
        }
        if(e.compareTo(root.e)<0){
            return contains(root.left,e);
        }else {
            return contains(root.right,e);
        }
    }

    /**
     * 树的遍历
     */
    public void preOrder(){
        preOrder(root);
    }

    private void preOrder(Node root){
        if(root ==null){
            return;
        }
        System.out.println(root.e);
        preOrder(root.left);
        preOrder(root.right);
    }

}
   