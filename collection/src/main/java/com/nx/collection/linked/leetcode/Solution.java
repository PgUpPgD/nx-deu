package com.nx.collection.linked.leetcode;

public class Solution {

    //使用常规循环方式来删除链表中的指定的元素
    public ListNode removeElements(ListNode head, int val){
        while (head != null && head.val == val){
            //第一个节点是要删除的节点
            ListNode delNode = head;
            //让链表的引用指向第一个节点的下一个节点
            head = head.next;
            //回收
            delNode.next = null;
        }
        if (head == null){
            return head;
        }

        //待删除节点是中间节点
        ListNode prev = head;
        while (prev.next != null){
            if (prev.next.val == val){
                ListNode delNode = prev.next;
                prev.next = delNode.next;
                delNode.next = null;
            }else{
                prev = prev.next;
            }
        }
        return head;
    }

    //递归实现
    public ListNode removeElement(ListNode head, int val) {
        if (head == null){
            return head;
        }
        ListNode resNode = removeElement(head.next, val);
        if (head.val == val){
            return resNode;  //待删除元素是子链表的头节点
        }else {
            head.next = resNode;
            return head;
        }
    }

    public static void main(String[] args) {
        int[] nums = {1,2,6,3,4,5,6};
        int val = 6;
        ListNode head = new ListNode(nums);
        System.out.println(head);
        ListNode resListNode = (new Solution()).removeElement(head, val);
        System.out.println(resListNode);
    }

}
