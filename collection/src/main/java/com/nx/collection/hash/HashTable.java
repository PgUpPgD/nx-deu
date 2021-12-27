package com.nx.collection.hash;

import java.util.TreeMap;

public class HashTable<K,V> {
    private TreeMap<K, V>[] hashtable;
    private int size;
    private int m;

    public HashTable(int m){
        this.m = m;
        size = 0;
        hashtable = new TreeMap[m];
        for (int i = 0; i < m;i ++){
            hashtable[i] = new TreeMap<>();
        }
    }
    public HashTable(){
        this(10);
    }
    public int getSize(){
        return size;
    }

    //如果key存在那么修改，如果key不存在那么新增
    public void put(K key, V value){
        int index = hashKey(key);
        TreeMap<K, V> treeMap = hashtable[index];
        if (treeMap.containsKey(key)){
            treeMap.put(key, value);
        }else {
            treeMap.put(key, value);
            size++;
        }
    }

    private int hashKey(K key) {
        return (key.hashCode()&0x7fffffff)%m;
    }

    public V remove(K key){
        int index = hashKey(key);
        TreeMap<K, V> treeMap = hashtable[index];
        if(treeMap.containsKey(key)){
            V v = treeMap.remove(key);
            size--;
            return  v;
        }
        return null;
    }

    public V get(K key){
        return hashtable[hashKey(key)].get(key);
    }

    public static void main(String[] args) {
        HashTable<String, Object> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("one", 3);
        System.out.println(table.get("one"));
    }
}
