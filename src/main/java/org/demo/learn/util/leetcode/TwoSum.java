package org.demo.learn.util.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author luwt
 * @date 2021/5/21.
 */
public class TwoSum {

    static final int[] nums = {1, 3, 4, 5, 7, 9};

    public static void main(String[] args) {
        int[] sum = twoSum(nums, 10);
        System.out.println(Arrays.toString(sum));
    }

    static int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int rest = target - nums[i];
            if (map.containsKey(rest)) {
                return new int[]{i, map.get(rest)};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("不存在这样的两个数");
    }

}
