package com.longyi.csjl.datastructure.map;

/**
 * @author ly
 * @description 集合Map
 * @date 2021/2/2 20:36
 */
public interface MyMap <K,V>{

    /**
     * 新增
     * @param key
     * @param value
     */
    void add(K key,V value);

    /**
     * 删除
     * @param key
     * @return
     */
    V remove(K key);

    /**
     * 是否包含
     * @param key
     * @return
     */
    boolean contains(K key);

    /**
     * 获取值
     * @param key
     * @return
     */
    V get(K key);

    /**
     * 更新值
     * @param key
     * @param value
     */
    void set(K key,V value);

    /**
     * 获取size
     * @return
     */
    int getSize();


    /**
     * 判断是否为空
     * @return
     */
    boolean isEmpty();






}
