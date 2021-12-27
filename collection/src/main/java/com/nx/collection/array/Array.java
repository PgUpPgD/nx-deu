package com.nx.collection.array;

/**
 * 自己封装一个数组
 */
public class Array<E> {

    private E[] data;
    private int size;

    public Array(){
        this(10);
    }

    public Array(int capacity){
        data = (E[]) new Object[capacity];
    }

    public int getCapacity(){
        return data.length;
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void add(int index, E e){
        if (index < 0 || index > size){
            throw new RuntimeException("index >= 0 && index <= size, add failed");
        }
        //扩容
        if(size == data.length){
            resize(2 * data.length);
        }
        //移位index
        for (int i = size - 1; i >= index; i--){
            //把data[2] 赋值给 data[3] 进行后移
            data[i + 1] = data[i];
        }
        data[index] = e;
        size++;
    }

    public void resize(int capacity){
        E[] newData = (E[]) new Object[capacity];
        for (int i = 0; i < size; i++){
            newData[i] = data[i];
        }
        data = newData;
    }

    public void addFirst(E e){
        add(0, e);
    }

    public void addLast(E e){
        add(size, e);
    }

    public E get(int index){
        if (index < 0 || index > size){
            throw new RuntimeException("index >= 0 && index <= size, get failed");
        }
        return data[index];
    }

    public void set(int index, E e){
        if (index < 0 || index > size){
            throw new RuntimeException("index >= 0 && index <= size, set failed");
        }
        data[index] = e;
    }

    public int find(E e){
        for (int i = 0; i < size; i++){
            if (data[i] == e) return i;
        }
        return -1;
    }

    public boolean contains(E e){
        for (int i = 0; i < size; i++){
            if (data[i] == e) return true;
        }
        return false;
    }

    //删除元素
    public E remove(int index){
        if(index < 0 || index >= size)
            throw new IllegalArgumentException("Remove failed. Index is illegal.");

        E ret = data[index];
        for (int i = index + 1; i < size; i++) {
            data[i - 1] = data[i];
        }
        size--;
        //当数组大小为容量capacity的1/4时，缩容一半
        if (size == data.length / 4 && data.length/2 != 0){
            resize(data.length / 2);
        }
        return ret;
    }

    // 从数组中删除第一个元素, 返回删除的元素
    public E removeFirst(){
        return remove(0);
    }

    // 从数组中删除最后一个元素, 返回删除的元素
    public E removeLast(){
        return remove(size - 1);
    }

    // 从数组中删除元素e
    public void removeElement(E e){
        int index = find(e);
        if(index != -1) remove(index);
    }

    public E getLast() {
        return get(size-1);
    }

    public E getFirst() {
        return get(0);
    }

    @Override
    public String toString(){
        StringBuilder res = new StringBuilder();
        res.append(String.format("Array: size = %d , capacity = %d\n", size, data.length));
        res.append('[');
        for(int i = 0 ; i < size ; i ++){
            res.append(data[i]);
            if(i != size - 1)
                res.append(", ");
        }
        res.append(']');
        return res.toString();
    }

    public static void main(String[] args) {

        Array<Integer> arr = new Array<>();
        for(int i = 0 ; i < 10 ; i ++)
            arr.addLast(i);
        System.out.println(arr);

        arr.add(1, 100);
        System.out.println(arr);

        arr.addFirst(-1);
        System.out.println(arr);

        arr.remove(2);
        System.out.println(arr);

        arr.removeElement(4);
        System.out.println(arr);

        arr.removeFirst();
        System.out.println(arr);
    }

}
