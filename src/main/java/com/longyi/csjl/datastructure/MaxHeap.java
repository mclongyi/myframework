package com.longyi.csjl.datastructure;

import java.util.Map;

/**
 * @author ly
 * @description 最大堆  定义:根节点不小于子节点
 * @date 2021/2/3 21:40
 * @throw
 */
public class MaxHeap<E extends Comparable<E>>  {
    private MyArray<E> data;

    public MaxHeap(int capacity){
        data=new MyArray<>(capacity);
    }

    public MaxHeap(){
        data=new MyArray<>();
    }

    public int getSize(){
        return data.getSize();
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    private int parent(int index){
        if(index==0){
            throw new IllegalArgumentException("根节点没有父节点");
        }
        return (index-1)/2;
    }

    private int leftChild(int index){
        return index*2+1;
    }

    private int rightChild(int index){
        return index*2+2;
    }

    public void add(E e){
        data.addLast(e);
        siftUp(data.getSize()-1);
    }

    private void siftUp(int k){
        //拿当前父节点和当前节点比较 如果当前节点大于父节点继续上浮
        while (k>0 && data.get(parent(k)).compareTo(data.get(k))<0){
            data.swap(k,parent(k));
            k=parent(k);
        }
    }

    public E findMax(){
        return (E)data.get(0);
    }

    public E extractMax(){
        E ret=findMax();
        data.swap(0,data.getSize()-1);
        data.removeLast();
        siftDown(0);
        return  ret;
    }

    private void siftDown(int k){
        while (leftChild(k)<data.getSize()){
            int j=leftChild(k);
            if(j+1<data.getSize()){
                return;
            }
        }
    }
}    
   