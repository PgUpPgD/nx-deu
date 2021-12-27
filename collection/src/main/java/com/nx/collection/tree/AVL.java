package com.nx.collection.tree;

import java.util.*;

public class AVL<E extends Comparable<E>> {

    private class Node{
        public E e;
        public Node left;
        public Node right;
        public int height;
        public Node(E e){
            this.e = e;
            this.left = null;
            this.right = null;
            //维护一个节点高度的字段，判断是否平衡
            this.height=1;
        }
        public Node(){
        }

        public String toString(){
            return e+"";
        }
    }
    private Node root;
    private int size;
    public int size(){return size;}
    //添加节点构造bst
    public void addEle(E e){
        this.root =  addEle(root,e);
    }

    /**
     * AVL树两个条件
     * 1、BST
     * 2、平衡
     */
    public boolean isBST(){
        ArrayList<E> eles = new ArrayList<>();
        midOrder(eles);
        for (int i=1;i<eles.size();i++){
            if (eles.get(i-1).compareTo(eles.get(i))>0){
                return false;
            }
        }
        return true;
    }

    public boolean isBalanced(){
        return isBalanced(root);
    }

    private boolean isBalanced(Node node) {
        if (node == null) return true;
        int bf = getBalancedFactor(node);
        if (Math.abs(bf) > 1) return false;
        return isBalanced(node.left)&&isBalanced(node.right);
    }

    /**
     * 将元素e添加到以node为根的一个子树
     * @return 新插入了元素e以后新树的根
     */
    private Node addEle(Node node,E e) {
        if (node == null){
            size++;
            return new Node(e);
        }
        if(e.compareTo(node.e)<0){
            node.left=addEle(node.left, e);
        }else {
            node.right = addEle(node.right, e);
        }
        //插入新节点维持再平衡
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        int balancedFactor = getBalancedFactor(node);
        //LL
        if(balancedFactor > 1 && getBalancedFactor(node.left) >= 0){
            return rightRotate(node);
        }
        //RR
        if(balancedFactor<-1&&getBalancedFactor(node.right)<=0){
            return leftRotate(node);
        }
        //LR
        if (balancedFactor>1&&getBalancedFactor(node.left)<0){
            node.left =  leftRotate(node.left);
            return rightRotate(node);
        }
        //RL
        if (balancedFactor<-1&&getBalancedFactor(node.right)>0){
            node.right =  rightRotate(node.right);
            return leftRotate(node);
        }
        return node;
    }

    private int getBalancedFactor(Node node) {
        if (node==null) return 0;
        return getHeight(node.left)-getHeight(node.right);
    }

    private int getHeight(Node node) {
        if (node==null) return 0;
        return node.height;
    }

    // n是不平衡节点，r是新的根节点，a是符合avl的子树的根节点
    // 对节点n进行向右旋转操作，返回旋转后新的根节点r
    //        n                              r
    //       / \                           /   \
    //      r   T4     向右旋转 (n)        a     n
    //     / \       - - - - - - - ->    / \   / \
    //    a   T3                       T1  T2 T3 T4
    //   / \
    // T1   T2
    private Node rightRotate(Node node) {
        Node r = node.left;
        Node t3 = r.right;

        r.right = node;
        node.left = t3;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        r.height = Math.max(getHeight(r.left), getHeight(r.right)) + 1;
        return r;
    }

    // 对节点n进行向左旋转操作，返回旋转后新的根节点r
    //    n                             r
    //  /  \                          /   \
    // T1   r      向左旋转 (n)       n     a
    //     / \   - - - - - - - ->   / \   / \
    //   T2  a                     T1 T2 T3 T4
    //      / \
    //     T3 T4
    private Node leftRotate(Node node) {
        Node r = node.right;
        Node t2 = r.left;

        r.left = node;
        node.right = t2;
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        r.height = Math.max(getHeight(r.left), getHeight(r.right)) + 1;
        return r;
    }

    //搜索有个元素e
    public Node search(E e){
        return search(root,e);
    }

    //以node为根节点的树种查找元素e的节点
    private Node search(Node node, E e) {
        if (node==null) return null;
        if(e.compareTo(node.e)==0){
            return node;
        }else if(e.compareTo(node.e)<0){
            return search(node.left,e);
        }else {
            return search(node.right,e);
        }
    }

    //查找某个元素的父节点
    public Node searchParent(E e){
        return searchParent(root,e);
    }

    private Node searchParent(Node node, E e) {
        if (node==null) return null;
        if ((node.left!=null&&e.compareTo(node.left.e)==0)||(node.right!=null&&e.compareTo(node.right.e)==0)){
            return node;
        }else {
            if(node.left!=null&&e.compareTo(node.e)<0){
                return searchParent(node.left,e);
            }else if(node.right!=null&&e.compareTo(node.e)>0){
                return search(node.right,e);
            }else{
                return null;
            }
        }
    }

    //前序遍历
    public void preOrder(){
        preOrder(root);
    }

     //以node为根的bst前序遍历
    private void preOrder(Node node) {
        if (node==null) return;
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);

    }

    //中序遍历
    public void midOrder(List<E> eles){
        midOrder(root,eles);
    }

    private void midOrder(Node node,List<E> eles) {
        if (node==null) return;
        midOrder(node.left,eles);
        eles.add(node.e);
        midOrder(node.right,eles);
    }

    //非递归的前序遍历
    public void perOrderNR(){
        if(root==null) return;
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()){
            Node curNode = stack.pop();
            System.out.println(curNode.e);
            if(curNode.right!=null) stack.push(curNode.right);
            if(curNode.left!=null) stack.push(curNode.left);
        }
    }

    public void levelOrder(){
        if (root==null) return;
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(root);
        while (!queue.isEmpty()){
            Node curNode = queue.remove();
            System.out.println(curNode.e);
            if (curNode.left!=null) queue.add(curNode.left);
            if (curNode.right!=null) queue.add(curNode.right);
        }
    }

    //删除节点
    public void remove(E e){
        root =  remove(root,e);
    }

    /**
     * @param node 以node为根的树删除元素e
     * @return 返回删除了元素e之后额那新树根
     */
    private Node remove(Node node, E e) {
        if (node==null) return null;

        Node retNode;
        if (e.compareTo(node.e)<0){
            node.left = remove(node.left,e);
            return node;
        }else if(e.compareTo(node.e)>0){
            node.right = remove(node.right, e);
            retNode = node;
        }else {
            if (node.left==null){
                Node rightNode = node.right;
                node.right=null;
                size--;
                retNode =  rightNode;
            }else if (node.right==null){
                Node leftNode = node.left;
                node.left=null;
                size--;
                retNode =  leftNode;
            }else {
                //有两棵子树
                Node minNode = minNode(node.right);
                minNode.right = remove(node.right,minNode.e);
                minNode.left=node.left;
                node.left=node.right=null;
                retNode =  minNode;
            }
        }
        if(retNode ==null) return null;
        //维持再平衡
        //插入新节点维持再平衡
        retNode.height=Math.max(getHeight(retNode.left),getHeight(retNode.right))+1;
        int balancedFactor = getBalancedFactor(retNode);
        //LL
        if(balancedFactor>1&&getBalancedFactor(retNode.left)>=0){
            return rightRotate(retNode);
        }
        //RR
        if(balancedFactor<-1&&getBalancedFactor(retNode.right)<=0){
            return leftRotate(retNode);
        }
        //LR
        if (balancedFactor>1&&getBalancedFactor(retNode.left)<0){
            retNode.left =  leftRotate(retNode.left);
            return rightRotate(retNode);
        }

        //RL
        if (balancedFactor<-1&&getBalancedFactor(retNode.right)>0){
            retNode.right =  rightRotate(retNode.right);
            return leftRotate(retNode);
        }
        return retNode;
    }

    private Node minNode(Node node) {
        if (node.left==null) return node;
        return minNode(node.left);
    }

    public static void main(String[] args) {
        AVL avl = new AVL();
        avl.addEle(1);
        avl.addEle(2);
        avl.addEle(3);
        avl.addEle(4);
        avl.addEle(5);
        avl.addEle(6);
        avl.addEle(7);
        System.out.println(avl.isBalanced());
        System.out.println(avl.isBST()); //true
        avl.levelOrder();
    }
}
