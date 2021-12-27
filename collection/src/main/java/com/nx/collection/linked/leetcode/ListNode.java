package com.nx.collection.linked.leetcode;

/**
 * 力扣203题
 * 删除给定值val，在链表中的所有节点
 */
public class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val){
        this.val = val;
    }
    ListNode(int val, ListNode next){
        this.val = val;
        this.next = next;
    }

    public ListNode(int[] nums){
        if (nums==null||nums.length==0){
            throw new IllegalArgumentException("arr can not be empty");
        }
        this.val=nums[0];
        ListNode cur = this;
        for (int i = 1; i < nums.length; i++) {
            cur.next = new ListNode(nums[i]);
            cur = cur.next;
        }
    }

    // 以当前节点为头结点的链表信息字符串
    @Override
    public String toString(){

        StringBuilder s = new StringBuilder();
        ListNode cur = this;
        while(cur != null){
            s.append(cur.val + "->");
            cur = cur.next;
        }
        s.append("NULL");
        return s.toString();
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1,2,3,4,5,6};
        ListNode node = new ListNode(nums);
        System.out.println(node.toString());
    }


}
