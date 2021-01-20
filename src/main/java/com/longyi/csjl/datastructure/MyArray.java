package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description 手写一个简单的数组类
 * @date 2021/1/18 21:23
 * @throw
 */
public class MyArray<E> {

    private Object[] data;
    private int size;

    public MyArray(){
            this(10);
    }
    public MyArray(int capacity){
        data=(E[])new Object[capacity];
        size=0;
    }

    public int getSize(){
        return size;
    }

    public int getCapacity(){
        return data.length;
    }

    public boolean isEmpty(){
        return size==0;
    }

    public void addLast(E e){
        this.add(e,size);
    }

    public void addFirst(E e){
        this.add(e,0);
    }

    public Object[] getData(){
        return data;
    }

    /**
     * 向指定位置添加元素
     * @param e
     * @param index
     */
    public void add(E e,int index){
        if(size == data.length){
            throw new IllegalArgumentException(" add fail array is full");
        }
        //校验索引是否合法
        if(index<0 || index>data.length){
            throw new IllegalArgumentException("index is illegal ");
        }
        //采用后移的方式对数组进行移动
        for(int i=size-1;i>=index;i--){
            data[i+1]=data[i];
        }
        data[index]=e;
        size++;
    }


    public boolean contains(E e){
        for(int i=0;i<data.length;i++){
            if(data[i].equals(e)){
                return true;
            }
        }
        return false;
    }


    public int find(E e){
        for(int i=0;i<data.length;i++){
            if(data[i].equals(e)){
                return i;
            }
        }
        return -1;
    }

    /**
     * 删除指定位置的元素 采用向前移动的方式
     * @param index
     * @return
     */
    public E remove(int index){
        if(index<0 || index>data.length){
            throw new IllegalArgumentException("remove fail out of array bound");
        }
        E res=(E)data[index];
        for(int i=index+1;i<size;i++){
            data[i-1]=data[i];
        }
        size--;
        //防止内存泄露
        data[size]=null;
        return res;
    }

    public E removeFirst(){
        return  remove(0);
    }


    public E removeLast(){
        return remove(size);
    }

    public void removeElement(E e){
        int index = find(e);
        if(index !=-1){
            remove(index);
        }
    }





    @Override
    public String toString(){
        StringBuilder str=new StringBuilder();
        str.append("Array size is "+size ).append("capacity is "+data.length ).append("\r\n");
        str.append("[");
        for(int i=0;i<size;i++){
            if(i == size-1){
                str.append(data[i]);
            }else{
                str.append(data[i]).append(",");
            }
        }
        str.append("]");
        return str.toString();
    }



}    
   