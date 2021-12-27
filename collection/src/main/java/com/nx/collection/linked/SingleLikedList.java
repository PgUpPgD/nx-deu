package com.nx.collection.linked;

public class SingleLikedList<E> {

    private class Node{
        public E e;
        public Node next;
        public Node(E e, Node next){
            this.e = e;
            this.next = next;
        }
        public Node(){
            this.e = null;
            this.next = null;
        }
        public Node(E e){
            this.e = e;
            this.next = null;
        }
        @Override
        public String toString(){
            return e.toString();
        }
    }

    //虚拟头结点
    private Node dummyHead;
    private int size;
    public SingleLikedList(){
        dummyHead = new Node();
        size = 0;
    }
    public int getSize(){
        return size;
    }
    public boolean isEntity(){
        return size == 0;
    }

    //链表中添加元素
    public void add(int index, E e){
        if(index<0||index>size){
            throw  new RuntimeException("add failed ,illegal index");
        }
        //寻找index的前置节点, 开始从虚拟节点遍历链表
        Node prev = dummyHead;
        for (int i = 0; i < index; i++){
            prev = prev.next;
        }
        //将指针重新指向
        prev.next = new Node(e, prev.next);
        size++;
    }

    public void addFirst(E e){
        add(0, e);
    }
    public void  addLast(E e){
        add(size,e);
    }

    //给定索引值，返回元素
    public E get(int index){
        if(index<0||index>size){
            throw  new RuntimeException("get failed ,illegal index");
        }
        //虚拟节点指向的第一个节点
        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++) {
            //找到了index代表的节点
            cur = cur.next;
        }
        return cur.e;
    }
    public E getFirst(){
        return get(0);
    }
    public E getLast(){
        return get(size-1);
    }

    public boolean contains(E e){
        Node cur = dummyHead.next;
        while (cur != null){
            if (cur.e.equals(e)) return true;
            cur = cur.next;
        }
        return false;
    }

    //修改某个位置的元素
    public void set(int index, E e){
        if(index<0||index>size){
            throw  new RuntimeException("get failed ,illegal index");
        }
        Node cur = dummyHead.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }
        cur.e = e;
    }

    //删除某个索引所在位置的元素
    public E remove(int index){
        if(index<0||index>size){
            throw  new RuntimeException("remove failed ,illegal index");
        }
        Node pre = dummyHead;
        //寻找index的前置节点,
        for (int i = 0; i < index; i++) {
            pre = pre.next;
        }
        Node delNode = pre.next;
        //变动指针
        pre.next = delNode.next;
        delNode.next = null;
        size--;
        return delNode.e;
    }

    public E removeFirst(){
        return remove(0);
    }
    public E removeLast(){
        return remove(size-1);
    }

    //反转链表
    public void  reverse(){
        //第一个节点
        Node cur = dummyHead.next;
        Node next = cur.next;
        Node reverseHead = new Node();//新链表的虚拟头结点
        //第一个节点不是空
        while (cur != null){
            //第二个节点
            next = cur.next;
            //第一个节点的指向=新链表的头结点指向->为新链表的第一个节点
            cur.next = reverseHead.next;
            //cur赋值到新链表的第一个节点
            reverseHead.next=cur;
            //循环遍历
            cur = next;
        }
        dummyHead = reverseHead;
    }

    //给定了元素，删除这个元素
    public void removeEle(E e){
        Node pre = dummyHead;
        while (pre.next != null){
            if (pre.next.e.equals(e)){
                pre = pre.next;
                break;
            }
        }
        if (pre.next != null){
            Node delNode = pre.next;
            pre.next=delNode.next;
            delNode.next = null;
            size--;
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        Node cur = dummyHead.next;
        while (cur!=null){
            builder.append(cur+"->");
            cur = cur.next;
        }
        builder.append("NULL");
        return builder.toString();
    }

    public static void main(String[] args) {
        SingleLikedList<Integer> list = new SingleLikedList<>();
        for (int i = 0; i < 5; i++) {
            list.addFirst(i);
        }
        System.out.println(list.toString());
        list.reverse();
        System.out.println(list.toString());
    }


}


