package com.demo.learn.util.leetcode;

import com.demo.learn.util.leetcode.linkedList.ArrayToLinkedList;
import com.demo.learn.util.leetcode.linkedList.SingleListNode;

/**
 * 两数相加
 * @author luwt
 * @date 2021/5/25.
 */
public class AddTwoNum {

    public static void main(String[] args){
        // 模拟数据，数据1是342，按逆序排列，数据2是465，相加结果是807，期望输出为 {7, 0, 8}
        int[] l1 = {2, 4, 3}, l2 = {5, 6, 4, 1};
        SingleListNode nodes1 = ArrayToLinkedList.getNodes(l1);
        SingleListNode nodes2 = ArrayToLinkedList.getNodes(l2);
        SingleListNode.print(nodes1);
        SingleListNode.print(nodes2);
        SingleListNode twoNum = AddTwoNum(nodes1, nodes2);
        SingleListNode.print(twoNum);
    }

    public static SingleListNode AddTwoNum(SingleListNode l1, SingleListNode l2) {
        // 进位
        int carry = 0;
        SingleListNode node1 = l1, node2 = l2, newNode = null;
        // 当node1和node2有一个不为空或者有进位，就继续循环
        while (node1 != null || node2 != null || carry == 1) {
            int value1 = 0, value2 = 0;
            if (node1 != null) {
                value1 = node1.value;
                node1 = node1.next;
            }
            if (node2 != null) {
                value2 = node2.value;
                node2 = node2.next;
            }
            // 计算相加结果
            int newValue = value1 + value2 + carry;
            // 相加的和如果大于9，就需要进位，当前位只保留个位数
            if (newValue > 9) {
                carry = 1;
                newValue = newValue - 10;
            } else {
                carry = 0;
            }
            // 构建正向链表，最后反转
            newNode = new SingleListNode(newValue, newNode);
        }
        return reverseList(newNode);
    }

    static SingleListNode reverseList(SingleListNode node) {
        if (node == null || node.next == null) {
            return node;
        }
        SingleListNode newNode = reverseList(node.next);
        node.next.next = node;
        node.next = null;
        return newNode;
    }

}
