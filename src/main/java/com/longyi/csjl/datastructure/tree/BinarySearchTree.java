package com.longyi.csjl.datastructure.tree;



import com.longyi.csjl.datastructure.ArrayStack;
import com.longyi.csjl.datastructure.MyStack;

import java.util.LinkedList;
import java.util.Queue;

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
     * 树的遍历  前序遍历（根节点--->左子树--- >右子树
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


    /**
     *  采用非递归方式的前序遍历
     */
    public void preOrderNR(){
        MyStack<Node> stack=new ArrayStack<>();
        stack.push(root);
        while (!stack.isEmpty()){
            Node cur = stack.pop();
            System.out.println(cur.e);
            if(cur.right!=null)
                stack.push(cur.right);
            if(cur.left!=null)
                stack.push(cur.left);
        }
    }

    /**
     * 广度优先算法
     */
    public void breadthOrder(){
        Queue<Node> queue=new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            Node cur = queue.remove();
            System.out.println(cur.e);
            if(cur.left!=null){
                queue.add(cur.left);
            }
            if(cur.right!=null){
                queue.add(cur.right);
            }
        }
    }

    /**
     * 获取二分搜索树的最小值
     * @return
     */
    public E getMinVal(){
        if(root==null)
            return null;
        return getMinVal(root).e;
    }

    private Node getMinVal(Node root){
        if(root.left==null)
            return root;
        return getMinVal(root.left);
    }

    public E  getMaxVal(){
        if(root == null)
            return null;
        return getMaxVal(root).e;
    }

    public Node getMaxVal(Node root){
        if(root.right == null)
            return root;
       return  getMaxVal(root.right);
    }

    /**
     * 移除最小值
     * @return
     */
    public E removeMin(){
        E e = getMinVal();
        removeMin(root);
        return e;
    }

    private Node removeMin(Node root){
        if(root.left ==null){
            Node newNode=root.right;
            root.right=null;
            size--;
            return newNode;
        }
        root.left=removeMin(root.left);
        return root;
    }


    /**
     * 删除树节点
     * @param e
     */
    public void remove(E e){
        root=remove(root,e);
    }

    private Node remove(Node root,E e){
        if(root ==null)
            return null;
        if(e.compareTo(root.e)<0){
         return    remove(root.left,e);
        } else if(e.compareTo(root.e)>0) {
          return   remove(root.right,e);
        }else{
            if(root.left ==null){
                Node rightNode=root.right;
                root.right=null;
                size--;
                return rightNode;
            }
            if(root.right ==null){
                Node left=root.left;
                root.left=null;
                size--;
                return left;
            }
           Node successor=getMinVal(root.right);
            successor.right=removeMin(root.right);
            successor.left=root.left;
            root.left=root.right=null;
            return successor;
        }

    }

    /**
     * 移除最大值
     * @return
     */
    public E removeMax(){
        E e = getMaxVal();

        return e;
    }

    private Node removeMax(Node root){
        if(root.right == null){
            Node leftNode=root.left;
            root.left=null;
            size--;
            return leftNode;
        }
        root.right=removeMax(root.right);
        return root;
    }

    /**
     * 树的中序遍历  先左子树--->根---->右子树
     */
    public void inOrder(){
        inOrder(root);
    }

    private void inOrder(Node root){
        if(root ==null)
            return;
        inOrder(root.left);
        System.out.println(root.e);
        inOrder(root.right);
    }

    /**
     * 后序遍历 先左子树-->右子树-->根节点
     */
    public void afterOrder(){
        afterOrder(root);
    }

    public void afterOrder(Node root){
        if(root == null)
            return;
        afterOrder(root.left);
        afterOrder(root.right);
        System.out.println(root.e);
    }




}
   