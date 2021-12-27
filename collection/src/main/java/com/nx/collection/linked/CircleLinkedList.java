package com.nx.collection.linked;

public class CircleLinkedList<E> {

    private class Node<E> {
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
        public String toString() {
            return e.toString();
        }
    }

    private Node head = null;
    private Node tail = null;

    public CircleLinkedList(int nums){
        if (nums < 1){
            throw new RuntimeException("nums<1!");
        }
        for (int i = 1; i <= nums; i++){
            Node node = new Node(i);
            if (i == 1){
                head = node;
                head.next = head;
                tail = node;
            }else {
                tail.next = node;
                node.next = head;
                tail = node;
            }
        }
    }
    public CircleLinkedList(){
        this(5);
    }

    /**
     * @param startNo 从第几个开始数，环形链表的head
     * @param countNum 数几次
     * @param nums 最开始的node的数目
     */
    public void exitCircle(int startNo,int countNum,int nums){
        if (head==null||startNo<1||startNo>nums){
            throw new RuntimeException("arr 错误！");
        }
        //找准头指针和尾指针的位置
        for (int i = 0; i < startNo - 1; i++) {
            head = head.next;
            tail = tail.next;
        }
        while (true){
            if (tail == head){
                break;
            }
            for (int i = 0; i < countNum - 1; i++) {
                head = head.next;
                tail = tail.next;
            }
            System.out.println(head.e + "元素出圈");
            //修正头尾指针位置，删除出圈元素
            head = head.next;
            tail.next = head;
        }
        System.out.println("最后圈内留下的元素："+head.e);
    }
    public static void main(String[] args) {
        CircleLinkedList<Object> circleLinkedList = new CircleLinkedList<>();
        circleLinkedList.exitCircle(1,2,5);
    }
}
