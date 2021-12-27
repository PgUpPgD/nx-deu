package com.nx.collection.tree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉查找树实现
 */
public class BST<E extends Comparable<E>> {

    private class Node{
        public E e;
        public Node left;
        public Node right;
        public Node(E e){
            this.e = e;
            this.left = null;
            this.right = null;
        }
        public Node(){}
        public String toString(){
            return e+"";
        }
    }

    private Node root;
    private int size;
    public int getSize(){
        return size;
    }
    //添加节点构造bst
    public void addEle(E e){
        this.root = addEle(root, e);
    }
    /**
     * 将元素e到以node为根的一个子树
     * @return 新插入了元素e以后新树的根
     */
    private Node addEle(Node node,E e) {
        if (node == null){
            size++;
            return new Node(e);
        }
        if (e.compareTo(node.e) < 0){
            node.left = addEle(node.left, e);
        }else {
            node.right = addEle(node.right, e);
        }
        return node;
    }

    //搜索有个元素e
    public Node search(E e){
        return search(root, e);
    }

    //以node为根节点的树种查找元素e的节点
    private Node search(Node node, E e) {
        if (node == null) return null;
        if (e.compareTo(node.e) == 0){
            return node;
        }else if(e.compareTo(node.e) < 0){
            return search(node.left, e);
        }else {
            return search(node.right, e);
        }
    }

    //查找某个元素的父节点
    public Node searchParent(E e){
        return searchParent(root,e);
    }

    private Node searchParent(Node node, E e) {
        if (node == null) return null;
        //父的左右子节点至少有一个不为空，且其中一个子节点的值为e
        if ((node.left != null && e.compareTo(node.left.e) == 0)
                || (node.right != null && e.compareTo(node.right.e) == 0)) {
            return node;
        }else {
            if (node.left != null && e.compareTo(node.e) < 0){
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
        if (node == null){
            return;
        }
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);
    }

    //中序遍历
    public void midOrder(){
        midOrder(root);
    }

    private void midOrder(Node node) {
        if (node==null){
            return;
        }
        midOrder(node.left);
        System.out.println(node.e);
        midOrder(node.right);
    }

    //非递归的前序遍历
    public void perOrderNR(){
        if (root == null){
            return;
        }
        Stack<Node> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()){
            Node curNode = stack.pop();
            System.out.println(curNode.e);
            if (curNode.right != null){
                stack.push(curNode.right);
            }
            if (curNode.left != null){
                stack.push(curNode.left);
            }
        }
    }

    //广度遍历
    public void levelOrder(){
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            Node curNode = queue.remove();
            System.out.println(curNode.e);
            if (curNode.left != null) queue.add(curNode.left);
            if(curNode.right != null) queue.add(curNode.right);
        }
    }

    //删除节点
    public void remove(E e){
        root = remove(root, e);
    }

    //以node为根的树删除元素e,返回删除了元素e之后额那新树根
    private Node remove(Node node, E e){
        if (node == null) return null;
        if (e.compareTo(node.e) < 0){
            node.left = remove(node.left, e);
            return node;
        }else if (e.compareTo(node.e) > 0){
            node.left = remove(node.right, e);
            return node;
        }else {
            if (node.left == null){
                Node rightNode = node.right;
                node.right = null;
                size--;
                return rightNode;
            }
            if (node.right == null){
                Node leftNode = node.left;
                node.left = null;
                size--;
                return leftNode;
            }
        }
        //有两颗子树
        Node minNode = minNode(node.right);
        //删除node右子树下的minNode(最小节点)，因为minNode替换node为根
        minNode.right = remove(node.right, minNode.e);
        minNode.left=node.left;
        node.left = node.right = null;
        return minNode;
    }

    //返回该node左子树的最小节点
    private Node minNode(Node node){
        if (node.left == null) return node;
        return minNode(node.left);
    }

    public static void main(String[] args) {
        BST bst = new BST();
        bst.addEle(7);
        bst.addEle(3);
        bst.addEle(10);
        bst.addEle(1);
        bst.addEle(5);
        bst.addEle(9);
        bst.addEle(12);
        System.out.println(bst.getSize());
        System.out.println(bst.search(5));
        System.out.println(bst.searchParent(5));
        bst.preOrder();
        bst.midOrder();
        bst.perOrderNR();
        bst.levelOrder();
        bst.remove(7);
    }


}
