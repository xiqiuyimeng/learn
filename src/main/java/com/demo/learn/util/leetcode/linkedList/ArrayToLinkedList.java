package com.demo.learn.util.leetcode.linkedList;

import com.demo.learn.util.leetcode.sort.QuickSort;

import java.util.Arrays;

/**
 * @author luwt
 * @date 2021/5/6.
 * 给定一个数组，构建成一个从小到大的链表
 */
public class ArrayToLinkedList {

    public static void main(String[] args) {
        int[] array = {3, 2, 5, 4, 1, 6};
        QuickSort.sort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
        SingleListNode.print(getNodes(array));
    }

    // 构建链表 n1 -> n2 -> n3 -> n4 -> n5 -> ...
    static SingleListNode getNodes(int[] array) {
        SingleListNode node = new SingleListNode(array[array.length - 1], null);
        int i = array.length - 2;
        while (i >= 0) {
            node = new SingleListNode(array[i], node);
            i --;
        }
        return node;
    }

}
