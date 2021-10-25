package org.demo.learn.util;

/**
 * @author luwt
 * @date 2021/1/13.
 */
public class FindMissNum {

    // 从0到n之间取出n个不同的数，找出漏掉的那个。 注意：你的算法应当具有线性的时间复杂度。你能实现只占用常数额外空间复杂度的算法吗？

    public static int findMissNumber(int[] nums) {
        int bitset = 0;
        for (int num : nums) {
            // 将对应位置为1
            bitset |= 1 << num;
        }
        for (int i = 0; i < nums.length; i++) {
            // 尝试每一位，找出等于0的那一位
            if ((bitset & 1 << i) == 0) {
                return i;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
//        int[] array = {0, 1, 2, 3, 5};
//        System.out.println(findMissNumber(array));
        findNum();
    }

    /**
     * 给定一个数组，其中有一个数字出现了一次，其余的都出现两次，找到出现一次的数
     */
    public static void findNum() {
        // 异或满足：两个相同数异或为0，任何数与0异或都是本身，满足交换律
        // 1 ^ 3 ^ 2 ^ 1 ^ 3 = 1 ^ 1 ^ 3 ^ 3 ^ 2 = 0 ^ 0 ^ 2 = 2
        int[] arr = {1, 3, 2, 1, 3};
        int tmp = 0;
        for (int value : arr) {
            tmp ^= value;
        }
        System.out.println(tmp);
    }

}
