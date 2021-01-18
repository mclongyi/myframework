package com.longyi.csjl.source.code;

import java.util.Map.Entry;

/**
 * @author ly
 * @description TODO
 * @date 2021/1/17 22:45
 * @throw
 */
public class MyHashMap<K,V> {

    private static final int DEFAULT_SIZE=1<<4;
    private Entry<K,V>[] data;
    private int size=0;
    private int cap;
    public MyHashMap(){
        this(DEFAULT_SIZE);
    }
    public MyHashMap(int cap){
        if(cap>0){
            data=new Entry[cap];
            size=0;
            this.cap=cap;
        }
    }

    public void put(K key,V value){
        int hash=hash(key);
        Entry<K,V> newE=new Entry<K,V>(key,value,null);
        Entry<K,V> hasE=data[hash];
        while (hasE!=null){
            if(hasE.key.equals(key)){
                hasE.value=value;
                return;
            }
            //连表的遍历
            hasE=hasE.next;
        }
        //如果没有是连表的插入
        newE.next=data[hash];
        data[hash]=newE;
        size++;
    }

    private int hash(K key){
        int h=0;
        if(key == null) {
            h=0;
        } else {
            h=key.hashCode()^(h>>>16);
        }
        return h%cap;
    }
    public V get(K key){
        int hash=hash(key);
        Entry<K,V> entry=data[hash];
        while (entry!=null){
            if(entry.key.equals(key)){
                return entry.value;
            }
            entry=entry.next;
        }
        return null;
    }
    /**
     * 数据结构
     */
    private class Entry<K,V>{
        private K key;
        private V value;
        private Entry<K,V> next;
        private int cap;


        public Entry(K key,V value,Entry<K,V> next){
            this.key=key;
            this.value=value;
            this.next=next;
        }

    }
}    
   