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
        // 递归法分析，原链表：n1 -> n2 -> n3 -> n4 -> n5，递归搜索尾结点，建立5个方法栈
        // 入栈过程：
        // stack1：n1，将 n1.next 也就是n2传入下一栈
        // stack2: n2，将 n2.next 也就是n3传入下一栈
        // stack3: n3，将 n3.next 也就是n4传入下一栈
        // stack4: n4，将 n4.next 也就是n5传入下一栈
        // stack5: n5，搜索到尾结点n5，返回结果退出当前栈，n5在最终反转结束后就是头结点，所以返回头结点即可。
        // 出栈过程：
        // stack4: 对于stack4来说，传入下一栈的是 head.next = n5，在stack4中head就是n4，将n4.next也就是n5的next指针指向n4，
        //          此时存在两个指向，n4 -> n5, n5 -> n4，这是环形链表，所以需要把原来的 n4 -> n5断开，
        //          也就是 n4.next = null，将搜索到的头结点返回到上一栈，代码执行结束，退出当前栈
        // stack3：当前head为n3，同样的，n3.next.next = n3 也就是 n4.next = n3，此时n3仍然指向n4，
        //          所以断开它，n3.next = null，将搜索到的头结点返回到上一栈，代码结束，退出当前栈
        // stack2：当前head为n2，同样，n2.next.next = n2 也就是 n3.next = n2，此时n2仍然指向n3，
        //          所以断开，n2.next = null，将搜索到的头结点返回到上一栈，代码结束，退出当前栈
        // stack1：当前head为n1，同样，n1.next.next = n1 也就是 n2.next = n1，此时n1仍然指向n2，
        //          所以断开，n1.next = null，将搜索到的头结点返回，代码结束，退出当前栈
        // 至此为止，递归产生的所有栈执行完毕，方法退出返回结果
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
        SingleListNode.print(headNode);
//        SingleListNode node = iterate(node1);
//        print(node);
        SingleListNode recurNode = recursion(headNode);
        SingleListNode.print(recurNode);
    }


}
