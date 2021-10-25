package org.demo.learn.util.leetcode.linkedList;

/**
 * @author luwt
 * @date 2021/4/16.
 */
public class GetKthFromEnd {


    public static SingleListNode getKthFromEnd(SingleListNode head, int k) {
        // 双指针法
        SingleListNode front = head, behind = head;
        // 快指针，先遍历k步
        while (front != null && k > 0) {
            front = front.next;
            k --;
        }
        // 慢指针，等快指针遍历k步后，与块指针一起遍历，等快指针遍历结束，返回慢指针
        while (front != null) {
            front = front.next;
            behind = behind.next;
        }
        return behind;
    }

    public static void main(String[] args) {
        SingleListNode headNode = SingleListNode.getTestData();
        SingleListNode kthFromEnd = getKthFromEnd(headNode, 2);
        assert kthFromEnd.value == 4;
    }

}
