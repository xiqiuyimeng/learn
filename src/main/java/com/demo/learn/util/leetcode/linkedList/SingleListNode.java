package com.demo.learn.util.leetcode.linkedList;

/**
 * @author luwt
 * @date 2021/4/16.
 */
public class SingleListNode {

    public int value;

    public SingleListNode next;

    public SingleListNode(int value, SingleListNode next) {
        this.value = value;
        this.next = next;
    }

    public static SingleListNode getTestData() {
        // n1 -> n2 -> n3 -> n4 -> n5
        int[] array = {1, 2, 3, 4, 5};
        return ArrayToLinkedList.getNodes(array);
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
