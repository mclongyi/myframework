package com.longyi.csjl.datastructure;

/**
 * @author ly
 * @description 手写一个简单的数组类
 * @date 2021/1/18 21:23
 * @throw
 */
public class MyArray {

    private int[] data;
    private int size;

    public MyArray(){
            this(10);
    }
    public MyArray(int capacity){
        data=new int[capacity];
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

    public void addLast(int e){
        this.add(e,size);
    }

    public void addFirst(int e){
        this.add(e,0);
    }

    public int[] getData(){
        return data;
    }

    /**
     * 向指定位置添加元素
     * @param e
     * @param index
     */
    public void add(int e,int index){
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


    public boolean contains(int e){
        for(int i=0;i<data.length;i++){
            if(data[i] == e){
                return true;
            }
        }
        return false;
    }


    public int find(int e){
        for(int i=0;i<data.length;i++){
            if(data[i] == e){
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
    public int remove(int index){
        if(index<0 || index>data.length){
            throw new IllegalArgumentException("remove fail out of array bound");
        }
        int res=data[index];
        for(int i=index+1;i<size;i++){
            data[i-1]=data[i];
        }
        size--;
        return res;
    }

    public int removeFirst(){
        return  remove(0);
    }


    public int removeLast(){
        return remove(size);
    }

    public void removeElement(int e){
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
   