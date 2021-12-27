package com.nx.collection.tree;

import java.util.LinkedList;
import java.util.Queue;

public class RBT <E extends Comparable<E>>{
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    public Node root;
    private int size;
    public  int size(){
        return size;
    }

    //添加节点,递归
    public void addEle(E e){
        root = addEle(root, e);
        root.color=BLACK;
    }

    //将元素E添加到以node为根的那个树上并且将新树的根返回
    private Node addEle(Node node, E e) {
        if (node == null){
            size++;
            return new Node(e);
        }

        if (e.compareTo(node.e) < 0){
            node.left =  addEle(node.left, e);
        }else {
            node.right =  addEle(node.right, e);
        }

        //左旋
        if(isRed(node.right) && !isRed(node.left)){
            node = leftRotate(node);
        }
        //右旋
        if (isRed(node.left)&&isRed(node.left.left)){
            node = rightRotate(node);
        }
        //颜色翻转
        if (isRed(node.right)&&isRed(node.left)){
            flipColors(node);
        }
        return node;
    }

    //   node                     x
    //  /   \     左旋转         /  \
    // T1   x   --------->   node   T3
    //     / \              /   \
    //    T2 T3            T1   T2
    private Node leftRotate(Node node){
        Node x = node.right;
        //旋
        node.right = x.left;
        x.left = node;

        x.color=node.color;
        node.color=RED;
        return x;
    }

    //颜色翻转
    private void flipColors(Node node){
        node.color=RED;
        node.left.color=BLACK;
        node.right.color=BLACK;

    }

    //     node                   x
    //    /   \     右旋转       /  \
    //   x    T2   ------->   y   node
    //  / \                       /  \
    // y  T1                     T1  T2
    private Node rightRotate(Node node){
        Node x = node.left;
        //旋转
        node.left=x.right;
        x.right=node;

        x.color=node.color;
        node.color=RED;
        return x;
    }

    private boolean isRed(Node node) {
        if (node==null) return BLACK;
        return node.color;
    }

    private class Node{
        public E e;
        public Node left;
        public Node right;
        public boolean color;

        public Node(E e) {
            this.e = e;
            this.left = null;
            this.right = null;
            this.color = RED;
        }
        @Override
        public String toString() {
            return "Node{" +
                    "e=" + e +
                    ", color=" + color +
                    '}';
        }
    }

    //层次遍历树
    public void levelOrder(){
        if(root==null){
            return;
        }
        Queue<Node> queue = new LinkedList();
        queue.add(root);
        while (!queue.isEmpty()){
            Node curNode = queue.remove();
            System.out.println(curNode);
            if (curNode.left!=null){
                queue.add(curNode.left);
            }
            if(curNode.right!=null){
                queue.add(curNode.right);
            }
        }
    }

    public static void main(String[] args) {
        RBT<Integer> rbt = new RBT<>();
        rbt.addEle(1);
        rbt.addEle(2);
        rbt.addEle(3);
        rbt.addEle(4);
        rbt.addEle(5);
        rbt.addEle(6);
        rbt.addEle(7);
        rbt.levelOrder();
    }
}
