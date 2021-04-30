package com.demo.learn.util.leetcode;

/**
 * @author luwt
 * @date 2021/4/30.
 * 给定一个正整数 M，取值范围 1 < M < 65536
 * 从1开始，每次可选的操作是 +1 或 *2，求要获取M，需要多少次操作
 * 思路：其实相当于，求当前M二进制的最高位，是1经过几次左移获得，最高位右边的低位是通过几次+1获得
 */
public class StepTest {

    public static void main(String[] args) {

        System.out.println(Step(1));

        System.out.println(Step(4));

        System.out.println(Step(5));

        System.out.println(Step(6));

        System.out.println(Step(7));

        System.out.println(Step(9));
    }

    static int Step(int m) {
        if (m >= 65536 || m <= 1) {
            return -1;
        }
        int step = 0;
        int tmp = m;
        while (true) {
            // 每次右移1位，这是在寻找最高位，当前的step就是1需要左移达到最高位的次数
            tmp = tmp >>> 1;
            step ++;
            if (tmp == 1) {
                break;
            }
        }
        // 利用求余算法，先获取出最高位表示的2的幂，然后 -1， 然后和原m做与运算，求出余数，这个余数就是必须通过 +1 获得的，所以就知道需要几步 +1
        step += m & ((1 << step) - 1);
        return step;
    }

}
