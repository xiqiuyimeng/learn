package org.demo.learn.util.leetcode.linkedList;

/**
 * @author luwt
 * @date 2021/5/25.
 * 找到指定的链表某个节点，比如中间节点
 */
public class GetSpecificNode {

    public static void main(String[] args) {
        SingleListNode data = SingleListNode.getTestData();
        SingleListNode middleNode = getOneThirdNode(data);
        SingleListNode.print(data);
        SingleListNode.print(middleNode);
    }

    /**
     * 找出中间节点，使用双指针法，快指针走两步，慢指针走一步
     * @Date 2021/5/25 15:47
     * @author luwt
     */
    static SingleListNode getMiddleNode(SingleListNode head) {
        SingleListNode fast = head, slow = head;
        while (fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow;
    }

    /**
     * 找到三分之一处的节点，快指针走三步，慢指针走一步
     * @Date 2021/5/25 15:54
     * @author luwt
     */
    static SingleListNode getOneThirdNode(SingleListNode head) {
        SingleListNode fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next.next;
            slow = slow.next;
        }
        return slow;
    }

}
