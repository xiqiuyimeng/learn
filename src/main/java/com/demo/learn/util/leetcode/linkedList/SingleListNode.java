package com.demo.learn.util.leetcode.linkedList;

/**
 * @author luwt
 * @date 2021/4/16.
 */
public class SingleListNode {

    int value;

    SingleListNode next;

    public SingleListNode(int value, SingleListNode next) {
        this.value = value;
        this.next = next;
    }

    public static SingleListNode getTestData() {
        // n1 -> n2 -> n3 -> n4 -> n5
        SingleListNode node5 = new SingleListNode(5, null);
        SingleListNode node4 = new SingleListNode(4, node5);
        SingleListNode node3 = new SingleListNode(3, node4);
        SingleListNode node2 = new SingleListNode(2, node3);
        return new SingleListNode(1, node2);
    }
}
