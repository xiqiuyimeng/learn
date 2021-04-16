package com.demo.learn.util.leetcode.linkedList;

/**
 * 反转链表
 * @author luwt
 * @date 2021/4/16.
 */
public class ReverseList {

    /**
     * 迭代法，对每一个节点进行处理
     */
    public static SingleListNode iterate(SingleListNode head) {
        SingleListNode next, prev = null, current = head;
        // 链表的最后一个节点指向的下一节点为空
        while (current != null) {
            // 保存当前节点的指针
            next = current.next;
            // 当前节点的指针指向prev
            current.next = prev;
            // 进行下一次循环，需要先将当前节点保存为prev，再对current赋值
            prev = current;
            current = next;
        }
        return prev;
    }

    /**
     * 递归法
     */
    public static SingleListNode recursion(SingleListNode head) {
        // 由于单向链表，如果先从头部开始反转，会找不到第三个节点，所以要从尾部开始反转
        // 空链表，或者最后一个节点直接返回，利用递归找到最后一个节点，然后将节点返回
        if (head == null || head.next == null) {
            return head;
        }
        SingleListNode node = recursion(head.next);
        // 首先反转最后一个节点，
        head.next.next = head;
        head.next = null;
        return node;
    }



    public static void main(String[] args) {
        // n1 -> n2 -> n3 -> n4 -> n5
        SingleListNode headNode = SingleListNode.getTestData();
        // 反转为 n5 -> n4 -> n3 -> n2 -> n1
        // n1 <- n2 <- n3 <- n4 <- n5
        print(headNode);
//        SingleListNode node = iterate(node1);
//        print(node);
        SingleListNode recurNode = recursion(headNode);
        print(recurNode);
    }

    public static void print(SingleListNode node) {
        str = "";
        show(node);
        System.out.println(str);
    }

    static String str = "";

    public static void show(SingleListNode node) {
        if (node.next != null) {
            str += node.value + " -> ";
            show(node.next);
        } else {
            str += node.value;
        }
    }


}
